package com.example.conanmoverandroidapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.R
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.auto_fragment.*
import kotlinx.android.synthetic.main.path_fragment.*


class PathFragment : Fragment() {

    private val TAG = "ReadData"
    private lateinit var database: DatabaseReference

    companion object {
        fun newInstance() = PathFragment()
    }

    private lateinit var viewModel: PathViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.path_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PathViewModel::class.java)
        // TODO: Use the ViewModel

        database = FirebaseDatabase.getInstance().reference;

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val path = snapshot.children
                    path.forEach {
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

        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }
}

