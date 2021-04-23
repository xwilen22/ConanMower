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
            startByte = self.port.read(1)
        
        returningList = [
            self.port.read(1), #Left or right
            self.port.read(1), #Relative angle change
            self.port.read(2), #Distance
            self.port.read(1)  #Stopped because of border
        ]
        return returningList

    ## Parses a list of bytes to a TraveledPath data class. The data class is returned.
    def getDataClass(self, retrievedBytesList):
        print("From data class: ", retrievedBytesList) # Kept for testing purposes.
        turnedLeft = int.from_bytes(retrievedBytesList[0], byteorder=BYTE_ORDER) == 1
        angleChange = int.from_bytes(retrievedBytesList[1], byteorder=BYTE_ORDER)

        # Dunno if negative angle is for turning left or vise versa but ye
        currentAngle = angleChange * -1 if turnedLeft else angleChange
        traveledDistance = int.from_bytes(retrievedBytesList[2], byteorder=BYTE_ORDER)
        stoppedByObstacle = int.from_bytes(retrievedBytesList[3], byteorder=BYTE_ORDER)

        return traveledPathData.TraveledPathData(currentAngle, traveledDistance, stoppedByObstacle)