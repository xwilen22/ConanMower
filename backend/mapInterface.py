from data.traveledPath import TraveledPathData
import pathSession, pyrebase, firebaseClient
from bottle import route, run, template, static_file

import webbrowser

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
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

@route('/')
def index():
    templateKeyValue = dict(
        # Where has the mower has gone in chronological order.
        points = getCurrentPoints()
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

# webbrowser.open('http://localhost:8080', new=1)
run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True, reloader=True, interval=1)