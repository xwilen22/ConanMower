import datetime

### This class contains all of the data used to store data in the TraveledPath document in the database. The data attributes are:
### - currentAngle: Int
### - traveledDistance: Int
### - endedByBorder: Bool
### - endTime: Datetime
class TraveledPathData:

    ## The constructor assigns each attribute with the input parameters sent from the mower.
    ## The current date time is also generated here.
    def __init__(self, currentAngle, traveledDistance, endedByBorder):
        self.endTime = datetime.datetime.now()
        self.currentAngle = currentAngle
        self.traveledDistance = traveledDistance
        self.endedByBorder = endedByBorder
    
    ## This function returns a dictionary consisting of each attribute used in the traveled path document in the database.
    def getDictionary(self):
        return {
            u'EndTime': self.endTime,
            u'CurrentAngle': self.currentAngle,
            u'TraveledDistance': self.traveledDistance,
            u'EndedByBorder': self.endedByBorder
        }