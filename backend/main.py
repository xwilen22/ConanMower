import firebaseClient as fc

### A connection to the database is established and used to insert an item in the database.
### This is currently test code that will be built upon. --- This comment can most likely be removed later. ---

traveledPathClient = fc.FirebaseClient(fc.TraveledPathSettings())

path = fc.TraveledPathData(20, 30, False)
traveledPathClient.InsertItem(path.getDictionary())

print("Inserted item.")