package com.example.conanmoverandroidapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.PathCanvas
import com.example.conanmoverandroidapp.R
import com.example.conanmoverandroidapp.TraveledPath
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.path_fragment.*
import kotlin.random.Random



class PathFragment :Fragment() {

    private val TAG = "ReadData"
    private lateinit var database: DatabaseReference
    private lateinit var viewModel: PathViewModel

    companion object {
        fun newInstance() = PathFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.path_fragment, container, false)

        //val rootView = PathCanvas(this.requireContext(),null)//inflater.inflate(R.layout.path_fragment, container, false)

        //return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        database = FirebaseDatabase.getInstance().reference;
        readDataFromRealtimeDatabase()

        //canvas = canvas_area
//        Log.d("VIEW7777",  canvas.context.toString())
        //canvas_area.context
        viewModel = ViewModelProviders.of(this).get(PathViewModel::class.java)
        //canvas = PathCanvas(this.requireContext(),null)
        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)

        }

    }

    private fun readDataFromRealtimeDatabase () {
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val data = snapshot.child("TraveledPath").children

                    data.forEach {
                        val traveledPath = it.getValue(TraveledPath::class.java)
                        Globals.traveledPathList.add(traveledPath!!)
                    }

                    Globals.traveledPathList.forEach {
                        Log.d(TAG, it.toString())
                    }
                }
                else{
                    Log.d(TAG, "snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Could not read from database", Toast.LENGTH_LONG).show()
            }

        })
    }
}
