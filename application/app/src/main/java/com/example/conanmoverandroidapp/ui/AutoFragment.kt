package com.example.conanmoverandroidapp.ui

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.R
import kotlinx.android.synthetic.main.auto_fragment.*


class AutoFragment : Fragment() {

    private var readyToReciveObstacleData = false

    private val ANIM_MOWER_DRIVE: Int = -2
    private val ANIM_OBJ_DETECTED: Int = -1
    private val ANIM_WALL_TURN_LEFT: Int = 0
    private val ANIM_OBJ_TURN_LEFT: Int = 1
    //private val ANIM_OBJ_TURN_RIGHT: Int = 3
    //private val ANIM_WALL_TURN_RIGHT: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auto_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val objectDetected = Observer<Int> { collisionObject ->
            // Update the UI
            if(readyToReciveObstacleData){
                playAnimationSequence(collisionObject)
            }
            readyToReciveObstacleData = true
        }
        Globals.databaseViewModel.objectionDetection.observe(viewLifecycleOwner, objectDetected)
        setUpNavigationButtons()
    }

    override fun onResume() {
        super.onResume()
        readyToReciveObstacleData = false
        Globals.databaseViewModel.initListenForObstacles()
        Globals.databaseViewModel.startListenForObstacles()
    }

    override fun onPause() {
        super.onPause()
        Globals.databaseViewModel.stopListenForObstacles()
    }

    private fun setUpNavigationButtons() {
        btn_path.setOnClickListener {
            val direction = AutoFragmentDirections.actionAutoFragmentToPathFragment()
            it.findNavController().navigate(direction)
            //playMowerAnimation(Random.nextInt(0, 5))
        }

        manual_button.setOnClickListener {
            val direction = AutoFragmentDirections.actionAutoFragmentToManualFragment()
            it.findNavController().navigate(direction)
        }

    }


    private fun playAnimationSequence(collisionObject: Int){
        if(collisionObject == ANIM_WALL_TURN_LEFT || collisionObject == ANIM_OBJ_TURN_LEFT){
            playMowerAnimation(ANIM_OBJ_DETECTED)
            anim_mower.repeatCount = 1
            anim_mower.addAnimatorListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    anim_mower.pauseAnimation()
                    Handler(Looper.getMainLooper()).postDelayed(Runnable { playMowerAnimation(collisionObject) }, 600)
                    Handler(Looper.getMainLooper()).postDelayed(Runnable { playMowerAnimation(ANIM_MOWER_DRIVE) }, 4000)
                }
            })
        } else {
            playMowerAnimation(ANIM_MOWER_DRIVE)
        }
    }

    /*For future use if needed*/
    private fun playMowerAnimation(anim: Int) {
        if(context != null){
            anim_mower.pauseAnimation()
            anim_mower.repeatCount = LottieDrawable.INFINITE
            when (anim) {
                ANIM_MOWER_DRIVE -> anim_mower.setAnimation(R.raw.anim_mower_drive)
                ANIM_OBJ_DETECTED -> anim_mower.setAnimation(R.raw.anim_obj_detected)
                ANIM_OBJ_TURN_LEFT -> anim_mower.setAnimation(R.raw.anim_obj_turn_left)
                //ANIM_OBJ_TURN_RIGHT -> anim_mower.setAnimation(R.raw.anim_obj_turn_right)
                ANIM_WALL_TURN_LEFT -> anim_mower.setAnimation(R.raw.anim_wall_turn_left)
               //ANIM_WALL_TURN_RIGHT -> anim_mower.setAnimation(R.raw.anim_wall_turn_right)
            }
            anim_mower.playAnimation()
        }
    }

}