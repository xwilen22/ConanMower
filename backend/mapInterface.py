from data.traveledPath import TraveledPathData
import pathSession, pyrebase
from bottle import route, run, template, static_file


HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
}

# 10 CM bak
def getCurrentPoints():
    testSession = pathSession.PathSession()
    return [
        testSession.getPointByTraveledData(TraveledPathData(None, True, 20, 10, True)),
        testSession.getPointByTraveledData(TraveledPathData([True, 20, 10, True])),
        testSession.getPointByTraveledData(TraveledPathData([True, 20, 10, True])),
        testSession.getPointByTraveledData(TraveledPathData([True, 20, 10, True])),
        testSession.getPointByTraveledData(TraveledPathData([False, 274, 13, False])),
        testSession.getPointByTraveledData(TraveledPathData([False, 50, 30, False]))
    ]

@route('/')
def index():
    templateKeyValue = dict(
        # Where has the mower gone in chronological order.
        # (X, Y, ByObstacle)
        points = getCurrentPoints()
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True)