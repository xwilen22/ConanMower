import data.traveledPath as traveledPath
import math
# X, Y
INITIAL_X = 0
INITIAL_Y = 0
INITIAL_POINT_TUPLE = (INITIAL_X, INITIAL_Y)
INITIAL_START_ANGLE = 0

REVERSE_MARGIN_CENTIMETERS = 10

class PathSession:
    def __init__(self):
        self.lastTraveledPathData = traveledPath.TraveledPathData([False, 0, 0, False])
        self.currentAngle = INITIAL_START_ANGLE
        self.lastPoint = INITIAL_POINT_TUPLE
    def getFirstPoint(self):
        return (INITIAL_X, INITIAL_Y, False)
    def getPointByTraveledData(self, traveledPathData=None):

        self.currentAngle = (self.currentAngle - traveledPathData.currentAngle) % 360
        traveledDistance = traveledPathData.traveledDistance - REVERSE_MARGIN_CENTIMETERS

        currentAngleSin = math.sin(math.radians(self.currentAngle))
        currentAngleCos = math.cos(math.radians(self.currentAngle))
        
        y = self.lastPoint[1] + currentAngleSin * traveledDistance
        x = self.lastPoint[0] + currentAngleCos * traveledDistance

        self.lastPoint = (x, y)
        print("New last point ", self.lastPoint)
        self.lastTraveledPathData = traveledPathData

        return (x, y, traveledPathData.stoppedByObstacle)

