from data.traveledPath import TraveledPathData

import pathSession, pyrebase, firebaseClient, math
from bottle import route, run, template, static_file

from operator import itemgetter

import webbrowser



HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost" #"192.168.43.8" # Raspberry IP for Honor 9 hotspot
}

MAP_SIZE_MARGIN_FACTOR = 3.5
X = 0
Y = 1
MIN = 0
MAX = 1


## get latest session points from the database
def getCurrentPoints():
    currentSessionNodes = firebaseClient.FirebaseClient("TraveledPath").getLatestSessionChildren()

    localSession = pathSession.PathSession()
    
    # (X, Y, ByObstacle)
    allPoints = [localSession.getFirstPoint()]

    for traveledPathNode in currentSessionNodes:
        allPoints.append(localSession.getPointByTraveledData(traveledPathNode))

    return allPoints

## Get the min and max points
def getMinMaxPoints(allPoints):
    # Declare empty list with necessary indexes to later be defined.
    minMaxPoints = ([allPoints[0][0], allPoints[0][0]], [allPoints[0][1], allPoints[0][1]])

    minMaxPoints[X][MIN] = min(allPoints, key=itemgetter(0))[0]
    minMaxPoints[X][MAX] = max(allPoints, key=itemgetter(0))[0]
    
    minMaxPoints[Y][MIN] = min(allPoints, key=itemgetter(1))[1]
    minMaxPoints[Y][MAX] = max(allPoints, key=itemgetter(1))[1]

    return minMaxPoints

## Get the points with highest & lowest x/y values, and return an average size for the map as tuple.
def getMapSizeTuple(allPoints):

    minMaxPoints = getMinMaxPoints(allPoints)

    mapWidth = abs(minMaxPoints[X][MIN] * MAP_SIZE_MARGIN_FACTOR) + abs(minMaxPoints[X][MAX] * MAP_SIZE_MARGIN_FACTOR)
    mapHeight = abs(minMaxPoints[Y][MIN] * MAP_SIZE_MARGIN_FACTOR) + abs(minMaxPoints[Y][MAX] * MAP_SIZE_MARGIN_FACTOR)

    return (mapWidth, mapHeight)

## 
def getMapCenterPoint(allPoints, height, width):
    
    minMaxPoints = getMinMaxPoints(allPoints)

    return (-((width + minMaxPoints[X][MIN] - minMaxPoints[X][MAX]) / 2), -((height + minMaxPoints[Y][MIN] - minMaxPoints[Y][MAX]) / 2))

@route('/')
def index():
    currentPointList = getCurrentPoints()
    currentMapSize = getMapSizeTuple(currentPointList)
    currentMapCenterPoint = getMapCenterPoint(currentPointList, currentMapSize[0], currentMapSize[1])

    print("Center point: ", currentMapCenterPoint)

    templateKeyValue = dict(
        # Where has the mower has gone in chronological order.
        points = currentPointList,
        # What coordinate should the map camera point at
        mapCenterPoint = currentMapCenterPoint,
        # Size of map, defined by height and width
        mapSize = currentMapSize
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True, reloader=False, interval=1)