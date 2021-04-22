# This is a placeholder for serialiser class.
# Parses data coming in from serial port to data class instance
import serial
import sys
import data.traveledPath as traveledPathData

BYTE_ORDER = sys.byteorder

def getSerialConnection(serialPortString):
    return serial.Serial(serialPortString, baudrate=9600, timeout=3.0)    

def getBytesOnRecieve(serialConnection):
    return list(
        serialConnection.read(1), #Left or right
        serialConnection.read(1), #Relative angle change
        serialConnection.read(2), #Distance
        serialConnection.read(1)  #Stopped because of border
    )

### Parses a list of bytes to a TraveledPath data class
def getDataClass(retrievedBytesList):
    print(retrievedBytesList)
    turnedLeft = int.from_bytes(retrievedBytesList[0], byteorder=BYTE_ORDER) == 1
    angleChange = int.from_bytes(retrievedBytesList[1], byteorder=BYTE_ORDER)

    # Dunno if negative angle is for turning left or vise versa but ye
    relativeAngle = angleChange * -1 if turnedLeft else angleChange
    traveledDistance = int.from_bytes(retrievedBytesList[2], byteorder=BYTE_ORDER)
    stoppedByObstacle = int.from_bytes(retrievedBytesList[3], byteorder=BYTE_ORDER)

    return traveledPathData.TraveledPathData(relativeAngle, traveledDistance, stoppedByObstacle)