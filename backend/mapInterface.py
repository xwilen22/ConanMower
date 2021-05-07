from data.traveledPath import TraveledPathData
import pathSession
from bottle import route, run, template, static_file

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
}

# 10 CM bak

@route('/')
def index():
    testSession = pathSession.PathSession()

    templateKeyValue = dict(
        # Where has the mower gone in chronological order.
        # (X, Y, ByObstacle)
        points = [
            testSession.getPointByTraveledData(TraveledPathData([True, 0, 30, True])),
            testSession.getPointByTraveledData(TraveledPathData([False, 0, 30, False])),
            testSession.getPointByTraveledData(TraveledPathData([False, 0, 30, False])),
            testSession.getPointByTraveledData(TraveledPathData([True, 0, 30, False]))
        ]
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True)