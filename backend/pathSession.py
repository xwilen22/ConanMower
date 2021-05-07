import data.traveledPath as traveledPath
import math
# X, Y
INITIAL_X = 50
INITIAL_Y = 50
INITIAL_POINT_TUPLE = (INITIAL_X, INITIAL_Y)
INITIAL_START_ANGLE = 0

class PathSession:
    def __init__(self):
        self.lastTraveledPathData = traveledPath.TraveledPathData([False, 0, 0, False])
        self.currentAngle = INITIAL_START_ANGLE
        self.lastPoint = INITIAL_POINT_TUPLE
    def getFirstPoint(self):
        return (INITIAL_X, INITIAL_Y, False)
    def getPointByTraveledData(self, traveledPathData=None):

        self.currentAngle = (self.currentAngle + traveledPathData.currentAngle) % 360
        #self.currentAngle = (self.currentAngle + traveledPathData.currentAngle) % 360

        currentAngleSin = math.sin(math.radians(self.currentAngle))
        currentAngleCos = math.cos(math.radians(self.currentAngle))

        # +Y +X 
        if 0 <= self.currentAngle <= 90:
            y = self.lastPoint[1] + currentAngleSin * traveledPathData.traveledDistance
            x = self.lastPoint[0] + currentAngleCos * traveledPathData.traveledDistance
        # +Y -X
        elif 90 < self.currentAngle <= 180:
            y = self.lastPoint[1] - currentAngleSin * traveledPathData.traveledDistance
            x = self.lastPoint[0] + currentAngleCos * traveledPathData.traveledDistance
        # -Y -X
        elif 180 < self.currentAngle < 270:
            y = self.lastPoint[1] - currentAngleSin * traveledPathData.traveledDistance
            x = self.lastPoint[0] - currentAngleCos * traveledPathData.traveledDistance
        # -Y +X
        else:
            y = self.lastPoint[1] + currentAngleSin * traveledPathData.traveledDistance
            x = self.lastPoint[0] - currentAngleCos * traveledPathData.traveledDistance
        
        self.lastPoint = (x, y)
        print("New last point ", self.lastPoint)
        self.lastTraveledPathData = traveledPathData

        return (x, y, traveledPathData.stoppedByObstacle)

