import firebaseClient as fc
import mowerSerialiser as ms
import data.traveledPath as traveledPathData

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient("TraveledPath")

serialPort = "/dev/serial0" # serial0 or ttys0 (being the same port) is used for Raspberry Pi Zero W.

serialConnection = ms.SerialConnection(serialPort)
while True:
    buffer = serialConnection.getBytesOnRecieve()
    if buffer != None:
        mowerData = traveledPathData.TraveledPathData(buffer).getDictionary()
        print("Mowerdata is: ", mowerData)
        traveledPathClient.InsertItem(mowerData)