import firebaseClient as fc
import mowerSerialiser as ms
import data.traveledPath as tpd

### Initialize client firebase client and serial connection.

traveledPathClient = fc.FirebaseClient("TraveledPath")

serialPort = "/dev/serial0" # serial0 or ttys0 (being the same port) is used for Raspberry Pi Zero W.
serialConnection = ms.SerialConnection(serialPort)

# Used to store data in memory until connection returns
unsentMowerData = []

while True:
    buffer = serialConnection.getBytesOnRecieve()
    if buffer != None:
        mowerData = tpd.TraveledPathData(buffer).getDictionary()
        session = traveledPathClient.getSession(buffer)
        traveledPathClient.checkIfNewSession(session)
        try:
            for index, data in enumerate(unsentMowerData):
                if data is not None:
                    traveledPathClient.insertItem(data)
                    unsentMowerData[index] = None
            unsentMowerData.clear()
            traveledPathClient.insertItem(mowerData)
        except Exception as error:
            unsentMowerData.append(mowerData)