package com.example.conanmoverandroidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        // TODO: Make PathCanvas is cleared before showing this fragment

        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }

        val pathSessionsObserver = Observer<MutableList<TraveledPathSession>> { sessions ->
            val sessionList = mutableListOf<String>()
            sessions.forEach { session ->
                sessionList.add(session.traveledPaths[0].EndTime.split(".")[0])
            }
            // TODO: Make sure data is ordered in correct way
            initiateSessionSpinner(sessionList)
        }
        Globals.pathViewModel.traveledPathSessions.observe(viewLifecycleOwner, pathSessionsObserver)

        if(Globals.traveledPathSessionList.isEmpty()){
            Globals.pathViewModel.readDataFromRealtimeDatabase()
        }
    }

    private fun initiateSessionSpinner(sessions: MutableList<String>){
        val spinner: Spinner = spinner
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
                    Globals.pathViewModel.changedSessionSelection.value = position
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

}
