import pyrebase
import datetime
import data.traveledPath as tp

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
        #self._documentTraveledPath = self.db.child(str(collectionName))

        print("Connected to client.")

    ## This function is used to insert an item into the database.
    def InsertItem(self, dataDict):        
        self.db.child(self.path).push(dataDict)
        print("Inserted item.")