import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Use a service account
cred = credentials.Certificate('./conan_mower_access_key.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

print("Finished runing")
print("db object:", db)

doc_ref = db.collection(u'users').document(u'alovelace')
doc_ref.set({
    u'first': u'Ada',
    u'last': u'Lovelace',
    u'born': 1815
})