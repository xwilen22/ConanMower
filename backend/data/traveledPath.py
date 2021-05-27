import datetime

### This class contains all of the data used to store data in the TraveledPath document in the database. The data attributes are:
### - currentAngle: Int
### - traveledDistance: Int
### - endedByBorder: Bool
### - endTime: Datetime
class TraveledPathData:

    ## The constructor assigns each attribute with the input parameters sent from the mower.
    ## The current date time is also generated here.
    def __init__(self, data, turnedLeft=None, angleChange=None, traveledDistance=None, stoppedByObstacle=None):
        didMowerTurnLeft = turnedLeft if turnedLeft is not None else data[0]
        angleChange = angleChange if angleChange is not None else data[1]
        # Positive if mower turned left. Negative when turning right.
        currentAngle = angleChange * -1 if didMowerTurnLeft else angleChange 

        # Set parameters
        self.endTime = datetime.datetime.now()
        self.currentAngle = currentAngle
        self.traveledDistance = traveledDistance if traveledDistance is not None else data[2]
        self.stoppedByObstacle = stoppedByObstacle if stoppedByObstacle is not None else data[3]

    ## Override with the newly fetched datetime.
    def overrideTimeStamp(self, dateTimeStamp):
        self.endTime = dateTimeStamp

    ## Override with the newly fetched angle.
    def overrideAngle(self, newAngle):
        self.currentAngle = newAngle

    ## This function returns a dictionary consisting of each attribute used in the traveled path document in the database.
    def getDictionary(self):
        return {
            u'EndTime': str(self.endTime),
            u'CurrentAngle': self.currentAngle,
            u'TraveledDistance': self.traveledDistance,
            u'StoppedByObstacle': self.stoppedByObstacle
        }