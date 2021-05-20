package com.example.conanmoverandroidapp.ui

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.TraveledPath
import com.example.conanmoverandroidapp.TraveledPathSession
import com.google.firebase.database.*
import java.util.*


class DataBaseViewModel : ViewModel() {
    private lateinit var database: DatabaseReference

    private lateinit var obstacleEventListener: ChildEventListener


    val traveledPathSessions: MutableLiveData<MutableList<TraveledPathSession>> by lazy {
        MutableLiveData<MutableList<TraveledPathSession>>()
    }

    val changedSessionSelection: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val objectionDetection: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun initListenForObstacles(){
        database = FirebaseDatabase.getInstance().reference
        obstacleEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val traveledPath = TraveledPath(dataSnapshot.key!!)
                objectionDetection.value = traveledPath.StoppedByObstacle
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val traveledPath = TraveledPath(dataSnapshot.key!!)
                objectionDetection.value = traveledPath.StoppedByObstacle
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    Globals.currentActivity,
                    "Could not read from database",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

   fun startListenForObstacles(){
        database.addChildEventListener(obstacleEventListener)
    }

    fun stopListenForObstacles(){
        database.removeEventListener(obstacleEventListener)
    }

    fun readDataFromRealtimeDatabase () {
        database = FirebaseDatabase.getInstance().reference

        val sessions = database.child("TraveledPath").limitToLast(5)
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val traveledPathSession = TraveledPathSession(dataSnapshot.key!!, mutableListOf())
                dataSnapshot.children.forEach { pathData ->
                    val traveledPath = pathData.getValue(TraveledPath::class.java)
                    traveledPathSession.traveledPaths.add(traveledPath!!)
                }

                Globals.traveledPathSessionList.add(traveledPathSession)
                traveledPathSessions.value = Globals.traveledPathSessionList

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val session = Globals.traveledPathSessionList.find {
                    it.sessionName == dataSnapshot.key
                }
                val indexOfSession = Globals.traveledPathSessionList.indexOf(session)

                // Read updated values
                val traveledPathSession = TraveledPathSession(dataSnapshot.key!!, mutableListOf())
                dataSnapshot.children.forEach { it
                    val traveledPath = it.getValue(TraveledPath::class.java)
                    traveledPathSession.traveledPaths.add(traveledPath!!)
                }
                // Replace old value
                Globals.traveledPathSessionList[indexOfSession] = traveledPathSession
                traveledPathSessions.value = Globals.traveledPathSessionList
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                var traveledPathSessionList = mutableListOf<TraveledPathSession>()

                // Copy all sessions except the removed session
                Globals.traveledPathSessionList.forEach {
                    if(it.sessionName != dataSnapshot.key!!){
                        traveledPathSessionList.add(it)
                    }
                }
                Globals.traveledPathSessionList = traveledPathSessionList
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Should never occur
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    Globals.currentActivity,
                    "Could not read from database",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        sessions.addChildEventListener(childEventListener)
    }

}

