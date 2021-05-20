package com.example.conanmoverandroidapp.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.TraveledPath
import com.example.conanmoverandroidapp.TraveledPathSession
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PathViewModel : ViewModel() {
    val traveledPathSessions: MutableLiveData<MutableList<TraveledPathSession>> by lazy {
        MutableLiveData<MutableList<TraveledPathSession>>()
    }

    val changedSessionSelection: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

     fun readDataFromRealtimeDatabase () {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.child("TraveledPath").children

                    data.forEach {
                        val traveledPathSession = TraveledPathSession(it.key!!, mutableListOf())
                        val traveledPaths = it.children
                        traveledPaths.forEach { pathData ->
                            val traveledPath = pathData.getValue(TraveledPath::class.java)
                            traveledPathSession.traveledPaths.add(traveledPath!!)
                        }
                        Globals.traveledPathSessionList.add(traveledPathSession)
                    }
                    traveledPathSessions.value = Globals.traveledPathSessionList
                } else {
                    Log.d("ReadData", "snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    Globals.currentActivity,
                    "Could not read from database",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}