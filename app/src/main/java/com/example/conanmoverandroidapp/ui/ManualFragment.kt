package com.example.conanmoverandroidapp.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.*
import kotlinx.android.synthetic.main.manual_fragment.*


class ManualFragment: Fragment() {

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManualViewModel::class.java)
        // TODO: Use the ViewModel
        setUpNavigationButtons()

        // If not connected to bluetooth, try to connect
        if(!Globals.bluetoothConnectedStatus){
            PermissionHandler.handleBluetoothPermissionStatus {
                tryToConnect()
            }
        }

        forwardButton.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Forward")
            true
        }

        reverseButton.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Reverse")
            true
        }

        leftButton.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Left")
            true
        }

        rightButton.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Right")
            true
        }
    }

    private fun setDirectionOnAction(action: Int, direction: String) {
        if(Globals.bluetoothConnectedStatus){
            if (action == MotionEvent.ACTION_DOWN) {
                BluetoothConnectionHandler.directionButtonPressed(direction)
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                BluetoothConnectionHandler.directionButtonPressed("")
            }
        }
        else{
            Toast.makeText(
                Globals.currentActivity,
                getString(R.string.no_bluetooth_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setUpNavigationButtons()
    {
        auto_button.setOnClickListener {
            val direction = ManualFragmentDirections.actionManualFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }


    fun tryToConnect() {
        val deviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, deviceFilter)
        val btstateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, btstateFilter)

        BluetoothConnectionHandler.connectToArduino {
            BluetoothConnectionHandler.executeOnMainThread {
                Toast.makeText(
                    Globals.currentActivity,
                    R.string.bluetooth_unable_to_connect,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}