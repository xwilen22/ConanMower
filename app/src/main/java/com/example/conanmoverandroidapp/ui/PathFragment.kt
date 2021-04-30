package com.example.conanmoverandroidapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.conanmoverandroidapp.PathCanvas
import com.example.conanmoverandroidapp.R
import kotlinx.android.synthetic.main.auto_fragment.*
import kotlinx.android.synthetic.main.path_fragment.*
import kotlin.random.Random


class PathFragment :Fragment() {

    companion object {
        fun newInstance() = PathFragment()
    }

    private lateinit var viewModel: PathViewModel
    private lateinit var canvas: PathCanvas


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

        //canvas = canvas_area
//        Log.d("VIEW7777",  canvas.context.toString())
        //canvas_area.context
        viewModel = ViewModelProviders.of(this).get(PathViewModel::class.java)
        //canvas = PathCanvas(this.requireContext(),null)
        btn_close.setOnClickListener {
            //val direction = PathFragmentDirections.actionPathFragmentToAutoFragment()
            //it.findNavController().navigate(direction)
            val x = Random.nextInt(0, 5) * Random.nextFloat()
            val y = Random.nextInt(0, 5) * Random.nextFloat()
            val x2 = Random.nextInt(0, 5) * Random.nextFloat()
            val y2 = Random.nextInt(0, 5) * Random.nextFloat()
            //canvas.drawFromAttributes(x, y, x2, y2)
        }
    }
}