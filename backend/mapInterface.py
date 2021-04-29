import data.traveledPath
from bottle import route, run, template, static_file

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
}

@route('/')
def index():
    templateKeyValue = dict(
        points = [
            (100, 54),
            (31, 100),
            (55, 9)
        ],
        # How the edges would be connected
        # points[0] ➡ points[2] ➡ points[1]
        edges = [
            (0, 2),
            (2, 1)
        ]
    )

    return template('index', templateKeyValue)

@route('/public/<filename:path>')
def send_static(filename):
    return static_file(filename, root='./public')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True)