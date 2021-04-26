package com.example.conanmoverandroidapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.auto_fragment.*
import kotlinx.android.synthetic.main.path_fragment.*


class PathFragment : Fragment() {

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

        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }

}