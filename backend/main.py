import firebaseClient as fc
import mowerSerialiser as ms
import data.traveledPath as tpd

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient("TraveledPath")

serialPort = "/dev/serial0" # serial0 or ttys0 (being the same port) is used for Raspberry Pi Zero W.
serialConnection = ms.SerialConnection(serialPort)

while True:
    buffer = serialConnection.getBytesOnRecieve()
    if buffer != None:
        mowerData = tpd.TraveledPathData(buffer).getDictionary()
        session = traveledPathClient.getSession(buffer)
        if(traveledPathClient.newSession(session)):
            print("Mowerdata is: ", mowerData)
            traveledPathClient.insertItem(mowerData)