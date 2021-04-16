import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import datetime


### This class is used as a setup-class to store attributes connecting to the TraveledPath document in the Cloud Firestore database. 
class TraveledPathSettings:

    ## The constructor provides the collection and document names to the attributes.
    def __init__(self):
        self.collectionName = "TraveledPaths"
        self.documentName = "TraveledPath"

    ## Fetch the name of the TraveledPaths collection used in the database. This collection contains the document "TraveledPath".
    def getCollectionName(self):
        return self.collectionName

    ## Fetch the name of the TraveledPath document used in the database. This is where the rows of data is found.
    def getDocumentName(self):
        return self.documentName 


### This class contains all of the data used to store data in the TraveledPath document in the database. The data attributes are:
### - currentAngle: Int
### - traveledDistance: Int
### - endedByBorder: Bool
### - endTime: Datetime
class TraveledPathData:

    ## The constructor assigns each attribute with the input parameters sent from the mower.
    ## The current date time is also generated here.
    def __init__(self, currentAngle, traveledDistance, endedByBorder):
        self.endTime = datetime.datetime.now()
        self.currentAngle = currentAngle
        self.traveledDistance = traveledDistance
        self.endedByBorder = endedByBorder
    
    ## This function returns a dictionary consisting of each attribute used in the traveled path document in the database.
    def getDictionary(self):
        return {
            u'EndTime': self.endTime,
            u'CurrentAngle': self.currentAngle,
            u'TraveledDistance': self.traveledDistance,
            u'EndedByBorder': self.endedByBorder
        }


### This class handles the connection and each call to and from the database.
class FirebaseClient:

    ## The constructor establishes a connection to the database and assigns an attribute for each document used from the 
    ## database. Right now, it's just the TraveledPath document.
    def __init__(self, dataSettings):

        databaseCredentials = credentials.Certificate('./conan_mower_access_key.json')
        firebase_admin.initialize_app(databaseCredentials)

        db = firestore.client()
        self._documentTraveledPath = db.collection(dataSettings.getCollectionName()).document(dataSettings.getDocumentName())

        print("Connected to client.")

    ## This function is used to insert an item into the database.
    def InsertItem(self, documentDictionary):
        self._documentTraveledPath.set(documentDictionary)