import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import datetime

class FirebaseClient:

    def ___init___(self):
        # Use a service account
        credentials = credentials.Certificate('./conan_mower_access_key.json')
        firebase_admin.initialize_app(credentials)

        db = firestore.client()
        
        self.documentTraveledPath = db.collection(u'TraveledPaths').document(u'TraveledPath')

        print("Connected to client.")

    def InsertItem(self):
        time = datetime.datetime.now()
        
        self.documentTraveledPath.set({
            u'EndTime': time,
            u'CurrentAngle': 30,
            u'TravelledDistance': 0.2,
            u'EndedByBorder': True
        })
    
    
