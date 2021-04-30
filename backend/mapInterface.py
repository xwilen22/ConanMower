import data.traveledPath
from bottle import route, run, template, static_file

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
}

@route('/')
def index():
    templateKeyValue = dict(
        # Where has the mower gone in chronological order.
        # (X, Y, ByObstacle)
        points = [
            (10, 10, False),
            (10, 32, True),
            (50, 50, False),
            (20, 70, False)
        ]
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True)