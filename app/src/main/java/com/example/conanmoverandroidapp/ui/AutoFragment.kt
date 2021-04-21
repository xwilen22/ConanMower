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
import kotlin.random.Random


class AutoFragment : Fragment() {

    companion object {
        fun newInstance() = AutoFragment()
    }

    private lateinit var viewModel: AutoViewModel

    /*TODO Move to globals?*/
    private val ANIM_MOWER_DRIVE: Int = 0
    private val ANIM_OBJ_DETECTED: Int = 1
    private val ANIM_OBJ_TURN_LEFT: Int = 2
    private val ANIM_OBJ_TURN_RIGHT: Int = 3
    private val ANIM_WALL_TURN_LEFT: Int = 4
    private val ANIM_WALL_TURN_RIGHT: Int = 5

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
            //val direction = AutoFragmentDirections.actionAutoFragmentToPathFragment()
            //it.findNavController().navigate(direction)
            playMowerAnimation(Random.nextInt(0, 5))
        }

        manual_button.setOnClickListener {
            val direction = AutoFragmentDirections.actionAutoFragmentToManualFragment()
            it.findNavController().navigate(direction)
        }

    }

    private fun playMowerAnimation(anim: Int){
        when(anim) {
            ANIM_MOWER_DRIVE ->  anim_mower.setAnimation(R.raw.anim_mower_drive)
            ANIM_OBJ_DETECTED ->  anim_mower.setAnimation(R.raw.anim_obj_detected)
            ANIM_OBJ_TURN_LEFT -> anim_mower.setAnimation(R.raw.anim_obj_turn_left)
            ANIM_OBJ_TURN_RIGHT -> anim_mower.setAnimation(R.raw.anim_obj_turn_right)
            ANIM_WALL_TURN_LEFT -> anim_mower.setAnimation(R.raw.anim_wall_turn_left)
            ANIM_WALL_TURN_RIGHT -> anim_mower.setAnimation(R.raw.anim_wall_turn_right)
        }
        anim_mower.playAnimation()
    }

}