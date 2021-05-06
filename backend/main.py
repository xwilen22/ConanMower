import firebaseClient as fc
import mowerSerialiser as ms
import data.traveledPath as tpd

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient("TraveledPath")

serialPort = "/dev/serial0" # serial0 or ttys0 (being the same port) is used for Raspberry Pi Zero W.
serialConnection = ms.SerialConnection(serialPort)

while True:
    buffer = serialConnection.getBytesOnRecieve()
    print("buffer: ", buffer)
    if buffer != None:
        mowerData = tpd.TraveledPathData(buffer).getDictionary()
        print("early mower data: ", mowerData)
        session = traveledPathClient.getSession(buffer)
        print("The session: ", session)
        traveledPathClient.checkIfNewSession(session)
        print("Mowerdata is: ", mowerData)
        traveledPathClient.insertItem(mowerData)