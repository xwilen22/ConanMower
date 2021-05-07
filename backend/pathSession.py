import data.traveledPath as traveledPath
import math
# X, Y
INITIAL_POINT_TUPLE = (50, 50)
INITIAL_START_ANGLE = 0

class PathSession:
    def __init__(self):
        self.lastTraveledPathData = traveledPath.TraveledPathData([False, 0, 0, False])
        self.currentAngle = INITIAL_START_ANGLE
        self.lastPoint = INITIAL_POINT_TUPLE
    def getPointByTraveledData(self, traveledPathData):
        self.currentAngle = (self.currentAngle + traveledPathData.currentAngle) % 360
        print(self.currentAngle)
        #x = self.lastPoint[0] + math.cos(traveledPathData.currentAngle) * traveledPathData.traveledDistance
        # +Y +X
        if self.currentAngle >= 0 and self.currentAngle <= 90:
            y = self.lastPoint[1] + math.sin(self.currentAngle) * traveledPathData.traveledDistance
            x = self.lastPoint[0] + math.cos(self.currentAngle) * traveledPathData.traveledDistance
        # +Y -X
        elif self.currentAngle > 90 and self.currentAngle <= 180:
            y = self.lastPoint[1] + math.sin(self.currentAngle) * traveledPathData.traveledDistance
            x = self.lastPoint[0] - math.cos(self.currentAngle) * traveledPathData.traveledDistance
        # -Y -X
        elif self.currentAngle > 180 and self.currentAngle < 270:
            y = self.lastPoint[1] - math.sin(self.currentAngle) * traveledPathData.traveledDistance
            x = self.lastPoint[0] - math.cos(self.currentAngle) * traveledPathData.traveledDistance
        # -Y +X
        else:
            y = self.lastPoint[1] - math.sin(self.currentAngle) * traveledPathData.traveledDistance
            x = self.lastPoint[0] + math.cos(self.currentAngle) * traveledPathData.traveledDistance
        
        self.lastPoint = (x, y)
        print("New last point ", self.lastPoint)
        self.lastTraveledPathData = traveledPathData

        return (x, y, traveledPathData.stoppedByObstacle)

