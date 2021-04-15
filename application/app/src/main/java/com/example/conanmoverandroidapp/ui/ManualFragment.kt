package com.example.conanmoverandroidapp.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.R
import kotlinx.android.synthetic.main.auto_fragment.*

class ManualFragment : Fragment() {

    companion object {
        fun newInstance() = ManualFragment()
    }

    private lateinit var viewModel: ManualViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manual_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManualViewModel::class.java)
        // TODO: Use the ViewModel
        setUpNavigationButtons()

    }

    private fun setUpNavigationButtons()
    {

        auto_button.setOnClickListener {
            val direction = ManualFragmentDirections.actionManualFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }

    }

}