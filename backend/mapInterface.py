from data.traveledPath import TraveledPathData
import pathSession, pyrebase, firebaseClient, math
from bottle import route, run, template, static_file

from operator import itemgetter

import webbrowser

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost" #"192.168.43.8" # Raspberry IP for Honor 9 hotstop
}

# 10 CM bak
def getCurrentPoints():
    currentSessionNodes = firebaseClient.FirebaseClient("TraveledPath").getLatestSessionChildren()

    localSession = pathSession.PathSession()
    
    # (X, Y, ByObstacle)
    allPoints = [localSession.getFirstPoint()]

    for traveledPathNode in currentSessionNodes:
        allPoints.append(localSession.getPointByTraveledData(traveledPathNode))

    return allPoints

def getMapSizeTuple(allPoints):
    MAP_SIZE_MARGIN_FACTOR = 3

    lowestLargestY = [allPoints[0][1], allPoints[0][1]]
    lowestLargestX = [allPoints[0][0], allPoints[0][0]]

    lowestLargestX[0] = min(allPoints, key=itemgetter(0))[0]
    lowestLargestX[1] = max(allPoints, key=itemgetter(0))[0]
    
    lowestLargestY[0] = min(allPoints, key=itemgetter(1))[1]
    lowestLargestY[1] = max(allPoints, key=itemgetter(1))[1]
    
    mapWidth = abs(lowestLargestX[0] * MAP_SIZE_MARGIN_FACTOR) + abs(lowestLargestX[1] * MAP_SIZE_MARGIN_FACTOR)
    mapHeight = abs(lowestLargestY[0] * MAP_SIZE_MARGIN_FACTOR) + abs(lowestLargestY[1] * MAP_SIZE_MARGIN_FACTOR)

    return (mapWidth, mapHeight)

# 26.34859943472657 32.635340740183985

def getMapCenterPoint(allPoints):
    lowestLargestY = [allPoints[0][1], allPoints[0][1]]
    lowestLargestX = [allPoints[0][0], allPoints[0][0]]
    
    for point in allPoints:
        if point[0] < lowestLargestX[0]:
            lowestLargestX[0] = point[0]
        if point[0] > lowestLargestX[1]:
            lowestLargestX[1] = point[0]
        if point[1] < lowestLargestY[0]:
            lowestLargestY[0] = point[1]
        if point[1] > lowestLargestY[1]:
            lowestLargestY[1] = point[1]
    
    return (-3 * abs(lowestLargestX[0] - lowestLargestX[1]) / 2, -3 * abs(lowestLargestY[0] - lowestLargestY[1]) / 2)

@route('/')
def index():
    currentPointList = getCurrentPoints()
    currentMapSize = getMapSizeTuple(currentPointList)
    currentMapCenterPoint = getMapCenterPoint(currentPointList)

    print("Center point: ", currentMapCenterPoint)

    templateKeyValue = dict(
        # Where has the mower has gone in chronological order.
        points = currentPointList,
        mapCenterPoint = currentMapCenterPoint,
        mapSize = currentMapSize
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

# webbrowser.open('http://localhost:8080', new=1)
run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True, reloader=True, interval=1)