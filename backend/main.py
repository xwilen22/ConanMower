import firebaseClient as fc
import mowerSerialiser as ms
import data

### A connection to the database is established and used to insert an item in the database.
### This is currently test code that will be built upon. --- This comment can most likely be removed later. ---

traveledPathClient = fc.FirebaseClient(fc.TraveledPathSettings())

serialPort = "/dev/ttyS0" # ttyS0 is used for Raspberry Pi Zero. Normally it would've been ACM0. --- NOTE: This is a temporary comment that can be removed on release ---

serialConnection = ms.getSerialConnection(serialPort)

while True:
    serialConnection.write("Say something: ") # Temporary for test environment
    rcv = ms.getObjectOnRecieve(serialConnection)
    serialConnection.write("You sent: " + repr(rcv)) # Temporary for test environment

    # TODO:
    # 1. If the code above works, we need to convert the data to the correct format
    # 2. Add the converted data as parameters to data.TraveledPathData(), e.g. path = fc.TraveledPathData(currentAngle, traveledDistance, endedByBorder)

path = data.traveledPath.TraveledPathData(20, 30, False)
traveledPathClient.InsertItem(path.getDictionary())

print("Inserted item.")