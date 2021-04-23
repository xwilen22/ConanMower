import firebaseClient as fc
import mowerSerialiser as ms
import data

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient("TraveledPath")

serialPort = "/dev/ttyS0" # ttyS0 is used for Raspberry Pi Zero. Normally it would've been ACM0. --- NOTE: This is a temporary comment that can be removed on release ---

serialConnection = ms.SerialConnection(serialPort)

while True:
    byteArray = serialConnection.getBytesOnRecieve()
    print("ByteArray is", byteArray)
    if byteArray != None:
        mowerData = serialConnection.getDataClass(byteArray).getDictionary()
        print("Mowerdata is: ", mowerData)
        traveledPathClient.InsertItem(mowerData)

path = data.traveledPath.TraveledPathData(20, 30, False)
traveledPathClient.InsertItem(path.getDictionary())

print("Inserted item.")