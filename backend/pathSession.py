import data.traveledPath as traveledPath

class PathSession:
    def __init__(self):
        self.lastTraveledPathData = traveledPath.TraveledPathData(0, 0, False)
        self.lastPoint = (10, 10)
        pass
    def getPointByTraveledData(self, traveledPathData):
        self.lastTraveledPathData = traveledPathData
