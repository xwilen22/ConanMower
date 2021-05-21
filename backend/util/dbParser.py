import datetime
import sys

sys.path.append('../')
import data.traveledPath as tp

## Parse incoming data into correct format.
def parseSessionToDataClass(sessionItem):
    angleChange = sessionItem['CurrentAngle']

    stringFormat = "%Y-%m-%d %H:%M:%S"
    endTimeDateTime = datetime.datetime.strptime(sessionItem['EndTime'][0:19], stringFormat)
    
    stoppedByObstacle = sessionItem['StoppedByObstacle'] == 1
    traveledDistance = sessionItem['TraveledDistance']

    returningTraveledPathData = tp.TraveledPathData(None, turnedLeft=False, angleChange=0, traveledDistance=traveledDistance, stoppedByObstacle=stoppedByObstacle)
    
    returningTraveledPathData.overrideAngle(angleChange)
    returningTraveledPathData.overrideTimeStamp(endTimeDateTime)

    return returningTraveledPathData