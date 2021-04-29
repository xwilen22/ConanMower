import datetime

### This class contains all of the data used to store data in the TraveledPath document in the database. The data attributes are:
### - currentAngle: Int
### - traveledDistance: Int
### - endedByBorder: Bool
### - endTime: Datetime
class TraveledPathData:

    ## The constructor assigns each attribute with the input parameters sent from the mower.
    ## The current date time is also generated here.
    def __init__(self, data):
        turnedLeft = data[0]
        angleChange = data[1]
        # Positive if mower turned left. Negative when turning right.
        currentAngle = angleChange if turnedLeft else angleChange * -1 

        # set parameters
        self.endTime = datetime.datetime.now()
        self.currentAngle = currentAngle
        self.traveledDistance = data[2]
        self.stoppedByObstacle = data[3]
    
    ## This function returns a dictionary consisting of each attribute used in the traveled path document in the database.
    def getDictionary(self):
        return {
            u'EndTime': str(self.endTime),
            u'CurrentAngle': self.currentAngle,
            u'TraveledDistance': self.traveledDistance,
            u'StoppedByObstacle': self.stoppedByObstacle
        }