import firebaseClient as fc
import mowerSerialiser as ms
import data

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient(fc.TraveledPathSettings())

serialPort = "/dev/ttyS0" # ttyS0 is used for Raspberry Pi Zero. Normally it would've been ACM0. --- NOTE: This is a temporary comment that can be removed on release ---

serialConnection = ms.SerialConnection(serialPortString)

print(data)

while True:
    
    byteArray = serialConnection.getBytesOnRecieve()
    mowerData = serialConnection.getDataClass(byteArray).getDictionary()

    traveledPathClient.InsertItem(mowerData)

    #serialConnection.write("Say something: ") # Temporary for test environment
    #rcv = serialConnection.getBytesOnRecieve() # Temporary for test environment
    #serialConnection.write("You sent: " + repr(rcv)) # Temporary for test environment

    # TODO:
    # 1. If the code above works, we need to convert the data to the correct format (DONE)
    # 2. Add the converted data as parameters to data.TraveledPathData(), e.g. path = fc.TraveledPathData(currentAngle, traveledDistance, endedByBorder) (SHOULD BE DONE)

path = data.traveledPath.TraveledPathData(20, 30, False)
traveledPathClient.InsertItem(path.getDictionary())

print("Inserted item.")