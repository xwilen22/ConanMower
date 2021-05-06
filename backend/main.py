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

        if(traveledPathClient.newSession(buffer[4])): # buffer[4] is a flag from the mower to check if the mower just started a new session.
            print("Mowerdata is: ", mowerData)
            traveledPathClient.insertItem(mowerData)