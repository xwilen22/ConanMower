import data.traveledPath
from bottle import route, run, template

HOST_PROPERTIES = {
    "port":8080,
    "name":"localhost"
}

@route('/')
def index():
    return template('index', name='Ringus')

run(host=HOST_PROPERTIES["name"], port=HOST_PROPERTIES["port"], debug=True)