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


class AutoFragment : Fragment() {

    companion object {
        fun newInstance() = AutoFragment()
    }

    private lateinit var viewModel: AutoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auto_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AutoViewModel::class.java)
        // TODO: Use the ViewModel

        setUpNavigationButtons()

    }

    private fun setUpNavigationButtons()
    {
        btn_path.setOnClickListener {
            val direction = AutoFragmentDirections.actionAutoFragmentToPathFragment()
            it.findNavController().navigate(direction)
        }

        manual_button.setOnClickListener {
            val direction = AutoFragmentDirections.actionAutoFragmentToManualFragment()
            it.findNavController().navigate(direction)
        }

    }

}