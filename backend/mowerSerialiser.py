import serial
import sys

### This class sets up and handles a serial connection between the Raspberry and the mower. 
class SerialConnection():

    ## Creates the connection through a specified serial port.
    def __init__(self, serialPortString):
        self.port = serial.Serial(serialPortString, baudrate=9600) 
        self.port.flush()

    ## reads the first value in the buffer and parse it to an int. It returns an integer.
    def readByteToInt(self, nrOfBytes, endian='big'):
        return int.from_bytes(self.port.read(nrOfBytes), byteorder=endian)

    ## This method reads the data coming in from the arduino and returns it as a list.
    def getBytesOnRecieve(self):
        startByte = 254
        expectedNrOfBytes = 6
        buffer = []

        if self.port.inWaiting() >= expectedNrOfBytes and self.readByteToInt(1) == startByte:
            buffer.append( self.readByteToInt(1) ) # turned left
            buffer.append( self.readByteToInt(1) ) # relative turn (degrees)
            buffer.append( self.readByteToInt(2) ) # distance (centimeters)
            buffer.append( self.readByteToInt(1) ) # stopped because of obstacle
            buffer.append( self.readByteToInt(1) ) # flag for new session
            return buffer
        else:
            return None