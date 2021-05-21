package com.example.conanmoverandroidapp.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.R
import com.example.conanmoverandroidapp.TraveledPathSession
import kotlinx.android.synthetic.main.path_fragment.*


class PathFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.path_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpNavigationButton()

        val pathSessionsObserver = Observer<MutableList<TraveledPathSession>> { sessions ->
            val sessionList = mutableListOf<String>()
            sessions.asReversed().forEach { session ->
                val lastIndex = session.traveledPaths.count() - 1
                val start = session.traveledPaths[0].EndTime.split(".")[0]
                val end = session.traveledPaths[lastIndex].EndTime.split(".")[0]
                sessionList.add("$start - $end")
            }
            initiateSessionSpinner(sessionList)
        }
        Globals.databaseViewModel.traveledPathSessions.observe(viewLifecycleOwner, pathSessionsObserver)

        if(Globals.traveledPathSessionList.isEmpty()){
            Globals.databaseViewModel.readDataFromRealtimeDatabase()
        }
    }

    private fun setUpNavigationButton(){
        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun initiateSessionSpinner(sessions: MutableList<String>){
        spinner.adapter = null
        spinner.setPadding(10,10,10,10)
        spinner.gravity = Gravity.CENTER_HORIZONTAL
        ArrayAdapter(
            Globals.currentActivity,
            R.layout.spinner_item_main,
            sessions
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position >= 0){
                    Globals.databaseViewModel.changedSessionSelection.value = position
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }
}
