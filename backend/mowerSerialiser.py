# This is a placeholder for serialiser class.
# Parses data coming in from serial port to data class instance
import serial

def getSerialConnection(serialPortString):
    return serial.Serial(serialPortString, baudrate=9600, timeout=3.0)    

def getObjectOnRecieve(serialConnection):
    return serialConnection.read(5)

def formatRecievedObject(bytearray):
    pass