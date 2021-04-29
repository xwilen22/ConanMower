import serial
import sys
import data.traveledPath as traveledPathData

BYTE_ORDER = sys.byteorder

### This class sets up and handles a serial connection between the Raspberry and the mower. 
class SerialConnection():

    ## Creates the connection through a specified serial port.
    def __init__(self, serialPortString):
        self.port = serial.Serial(serialPortString, baudrate=9600) 
        self.port.flush()

    def readInt(self, nrOfBytes, endian='big'):
        return int.from_bytes(self.port.read(nrOfBytes), byteorder=endian)

    ## This method reads the data coming in from the arduino and returns it as a list.
    def getBytesOnRecieve(self):

        startByte = 254
        expectedNrOfBytes = 6
        buffer = []

        if self.port.inWaiting() >= expectedNrOfBytes and self.readInt(1) == startByte:
            buffer.append( self.readInt(1) ) # turned left
            buffer.append( self.readInt(1) ) # relative turn (degrees)
            buffer.append( self.readInt(2) ) # distance (centimeters)
            buffer.append( self.readInt(1) ) # stopped because of obstacle

            return buffer

        else:
            return None
        '''while len(returningList) < 6:
            dataToBeStored = self.port.read(1)
            if(dataToBeStored != b''):
                returningList.append(dataToBeStored)
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

        print("Bytes stored in the list: ", returningList)
        return returningList'''

    ## Parses a list of bytes to a TraveledPath data class. The data class is returned.
    def getDataClass(self, retrievedBytesList):
        print("From data class: ", retrievedBytesList) # Kept for testing purposes.
        turnedLeft = retrievedBytesList[0]
        angleChange = retrievedBytesList[1]

        currentAngle = angleChange * -1 if turnedLeft else angleChange

        traveledDistance = retrievedBytesList[2]
        stoppedByObstacle = retrievedBytesList[3]

        return traveledPathData.TraveledPathData(currentAngle, traveledDistance, stoppedByObstacle)