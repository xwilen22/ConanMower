import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import datetime

class TraveledPathSettings:
    def __init__(self):
        self.collectionName = "TraveledPaths"
        self.documentName = "TraveledPath"
    def getCollectionName(self):
        return self.collectionName
    def getDocumentName(self):
        return self.documentName 

class TraveledPathData:
    def __init__(self, currentAngle, traveledDistance, endedByBorder):
        self.endTime = datetime.datetime.now()
        self.currentAngle = currentAngle
        self.traveledDistance = traveledDistance
        self.endedByBorder = endedByBorder
    def getDictionary(self):
        return {
            u'EndTime': self.endTime,
            u'CurrentAngle': self.currentAngle,
            u'TraveledDistance': self.traveledDistance,
            u'EndedByBorder': self.endedByBorder
        }

class FirebaseClient:
    def __init__(self, dataSettings):
        # Use a service account
        databaseCredentials = credentials.Certificate('./conan_mower_access_key.json')
        firebase_admin.initialize_app(databaseCredentials)

        db = firestore.client()
        self._documentTraveledPath = db.collection(dataSettings.getCollectionName()).document(dataSettings.getDocumentName())

        print("Connected to client.")

    def InsertItem(self, documentDictionary):
        time = datetime.datetime.now()
        
        self._documentTraveledPath.set(documentDictionary)
    

