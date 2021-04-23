import serial
import sys
import data.traveledPath as traveledPathData

BYTE_ORDER = sys.byteorder

### This class sets up and handles a serial connection between the Raspberry and the mower. 
class SerialConnection():

    ## Creates the connection through a specified serial port.
    def __init__(self, serialPortString):
        self.port = serial.Serial(serialPortString, baudrate=9600, timeout=1.0)    

    ## This method reads the data coming in from the arduino and returns it as a list.
    def getBytesOnRecieve(self):
        startByte = self.port.read(1)
        while int.from_bytes(startByte, byteorder=BYTE_ORDER) != 254:
            print("start byte: ", startByte)
            startByte = self.port.read(1)
        
        leftOrRight = self.port.read(1)
        angleChange = self.port.read(1)
        
        distanceOne = self.port.read(1)
        distanceTwo = self.port.read(1)

        stoppedForObstacle = self.port.read(1)

        returningList = [
            leftOrRight, #Left or right
            angleChange, #Relative angle change
            distanceOne, #Distance
            distanceTwo,
            stoppedForObstacle  #Stopped because of border
        ]
        return returningList

    ## Parses a list of bytes to a TraveledPath data class. The data class is returned.
    def getDataClass(self, retrievedBytesList):
        print("From data class: ", retrievedBytesList) # Kept for testing purposes.
        turnedLeft = int.from_bytes(retrievedBytesList[0], byteorder=BYTE_ORDER) == 1
        angleChange = int.from_bytes(retrievedBytesList[1], byteorder=BYTE_ORDER)

        # Dunno if negative angle is for turning left or vise versa but ye
        currentAngle = angleChange * -1 if turnedLeft else angleChange

        traveledDistanceOne = int.from_bytes(retrievedBytesList[2], byteorder=BYTE_ORDER)
        traveledDistanceTwo = int.from_bytes(retrievedBytesList[3], byteorder=BYTE_ORDER)

        traveledDistance = traveledDistanceOne * 255 + traveledDistanceTwo

        stoppedByObstacle = int.from_bytes(retrievedBytesList[4], byteorder=BYTE_ORDER)

        return traveledPathData.TraveledPathData(currentAngle, traveledDistance, stoppedByObstacle)