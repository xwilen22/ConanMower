import firebaseClient as fc

print("Doing the save")

traveledPathClient = fc.FirebaseClient(fc.TraveledPathSettings())

path = fc.TraveledPathData(20, 30, False)
traveledPathClient.InsertItem(path.getDictionary())