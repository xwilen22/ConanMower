package com.example.conanmoverandroidapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.R
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

        btn_close.setOnClickListener {
            val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }

}
