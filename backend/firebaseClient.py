import pyrebase
import datetime

import data.traveledPath as tp

def parseSessionToDataClass(sessionItem):
    angleChange = sessionItem['CurrentAngle']

    stringFormat = "%Y-%m-%d %H:%M:%S"
    endTimeDateTime = datetime.datetime.strptime(sessionItem['EndTime'][0:19], stringFormat)
    
    stoppedByObstacle = sessionItem['StoppedByObstacle'] == 1
    traveledDistance = sessionItem['TraveledDistance']

    returningTraveledPathData = tp.TraveledPathData(None, turnedLeft=False, angleChange=0, traveledDistance=traveledDistance, stoppedByObstacle=stoppedByObstacle)
    
    returningTraveledPathData.overrideAngle(angleChange)
    returningTraveledPathData.overrideTimeStamp(endTimeDateTime)

    return returningTraveledPathData

### This class handles the connection and each call to and from the database.
class FirebaseClient:
    ## The constructor establishes a connection to the database and assigns an attribute for each 
    ## document used from the database. Right now, it's just the TraveledPath document.
    def __init__(self, collectionName):
        # Use a service account
        firebaseConfig = {
            "apiKey": "AIzaSyDofuzKpCiUkkFk3Q3y-FxoLh2E1eCuH88",
            "authDomain": "conanmower.firebaseapp.com",
            "databaseURL": "https://conanmower-default-rtdb.europe-west1.firebasedatabase.app/",
            "storageBucket": "conanmower.appspot.com",
            "serviceAccount": "./key/conan_mower_access_key.json"
        }

        firebase = pyrebase.initialize_app(firebaseConfig)

        self.db = firebase.database()
        self.path = collectionName

        print("Connected to client.")

    ## This function is used to insert an item into the database.
    def insertItem(self, dataDict):
        self.db.child(self.path).child(self.sessionId).push(dataDict)
        print("Inserted item.")

    ## Returns a flag from the mower to check if the mower just started a new session.
    def getSession(self, buffer):
        return buffer[4]

    ## This function checks if the mower just started a new session. If it did, a new session key is generated.
    def checkIfNewSession(self, session):
        if(session == 1):
            self.sessionId = self.db.generate_key()
    
    def getLatestSessionChildren(self):
        retrievedDictionary = list(self.db.child(self.path).get().val().items())[0][1]
        returningList = [
            parseSessionToDataClass(item) for key, item in retrievedDictionary.items()
        ]
        return returningList