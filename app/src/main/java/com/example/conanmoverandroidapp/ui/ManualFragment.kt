package com.example.conanmoverandroidapp.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.content.pm.PackageManager
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

        checkIfBluetoothLESupported()
        // If not connected to bluetooth, try to connect
        if(!Globals.bluetoothConnectedStatus && !Globals.bluetoothDiscoveringStatus){
            PermissionHandler.handleBluetoothPermissionStatus {
                tryToConnectBluetoothToArduino()
            }
        }

        forward_button.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Forward")
            true
        }

        reverse_button.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Reverse")
            true
        }

        left_button.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Left")
            true
        }

        right_button.setOnTouchListener { _, event ->
            setDirectionOnAction(event.action, "Right")
            true
        }
    }

    override fun onResume() {
        super.onResume()

        checkIfBluetoothLESupported()
        // If not connected to bluetooth, try to connect
        if(!Globals.bluetoothConnectedStatus && !Globals.bluetoothDiscoveringStatus){
            PermissionHandler.handleBluetoothPermissionStatus {
                tryToConnectBluetoothToArduino()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(Globals.bluetoothConnectedStatus){
            BluetoothConnectionHandler.initiateAutoMowerControl()
        }
    }

    private fun checkIfBluetoothLESupported() {
        if(!Globals.currentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(Globals.currentActivity, getString(R.string.bluetooth_le_not_supported), Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
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

    private fun setUpNavigationButtons() {
        auto_button.setOnClickListener {
            val direction = ManualFragmentDirections.actionManualFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }

    private fun tryToConnectBluetoothToArduino() {
        val deviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, deviceFilter)
        val btStateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, btStateFilter)

        changeBluetoothConnectionUi(null)
        BluetoothConnectionHandler.initiateBluetoothConnectionToArduino { success ->
            Globals.executeOnMainThread {
                changeBluetoothConnectionUi(success)
                if(!success){
                    Toast.makeText(
                        Globals.currentActivity,
                        R.string.bluetooth_unable_to_connect,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun changeBluetoothConnectionUi(couldConnect: Boolean?){
        if(anim_bt != null && anim_bt.isAnimating){
            anim_bt.visibility = View.GONE
            anim_bt.cancelAnimation()
            if(couldConnect!!){
                bt_indicator.visibility = View.VISIBLE
                bt_indicator.setImageResource(R.drawable.ic_bluetooth_connected)
            } else {
                bt_indicator.visibility = View.VISIBLE
                bt_indicator.setImageResource(R.drawable.ic_bluetooth_disconnected)
            }
        } else if(anim_bt != null){
            bt_indicator.visibility = View.GONE
            anim_bt.visibility = View.VISIBLE
            anim_bt.playAnimation()
        }
    }

}