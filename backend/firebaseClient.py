import pyrebase
import datetime
import data.traveledPath as tp

class FirebaseClient:
    def __init__(self, forCollectionName):
        # Use a service account
        firebaseConfig = {
            "apiKey": "AIzaSyDofuzKpCiUkkFk3Q3y-FxoLh2E1eCuH88",
            "authDomain": "conanmower.firebaseapp.com",
            "databaseURL": "https://conanmower-default-rtdb.europe-west1.firebasedatabase.app/",
            "storageBucket": "conanmower.appspot.com",
            "serviceAccount": "./conan_mower_access_key.json"
        }

        firebase = pyrebase.initialize_app(firebaseConfig)

        db = firebase.database()
        
        print("DB is: ", db)
        self._documentTraveledPath = db.child(str(forCollectionName))

        print("Connected to client.")

    def InsertItem(self, dataDict):        
        self._documentTraveledPath.push(dataDict)