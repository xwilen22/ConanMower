import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import datetime

class FirebaseClient:

    def __init__(self):
        # Use a service account
        databaseCredentials = credentials.Certificate('./conan_mower_access_key.json')
        firebase_admin.initialize_app(databaseCredentials)

        db = firestore.client()
        print("DB is: ", db)
        self._documentTraveledPath = db.collection(u'TraveledPaths').document(u'TraveledPath')

        print("Connected to client.")

    def InsertItem(self):
        time = datetime.datetime.now()
        
        self._documentTraveledPath.set({
            u'EndTime': time,
            u'CurrentAngle': 30,
            u'TravelledDistance': 0.2,
            u'EndedByBorder': True
        })
    
    
