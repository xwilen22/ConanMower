# This is a placeholder for serialiser class.
# Parses data coming in from serial port to data class instance
import serial
import data.traveledPath as traveledPathData

def getSerialConnection(serialPortString):
    return serial.Serial(serialPortString, baudrate=9600, timeout=3.0)    

def getObjectOnRecieve(serialConnection):
    return serialConnection.read(5)

# Returns parsed 
def getDataClass(bytearray):
    traveledPathData.TraveledPathData(11, 11, False)
    pass