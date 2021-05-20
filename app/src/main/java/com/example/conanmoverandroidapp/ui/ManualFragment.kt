package com.example.conanmoverandroidapp.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.BluetoothConnectionHandler
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.PermissionHandler
import com.example.conanmoverandroidapp.R
import kotlinx.android.synthetic.main.manual_fragment.*


class ManualFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.manual_fragment, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val connectObserver = Observer<Boolean> { success ->
            // Update the UI
            changeBluetoothConnectionUi(success)
            if (!success) {
                Toast.makeText(
                    Globals.currentActivity,
                    R.string.bluetooth_unable_to_connect,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        Globals.bluetoothViewModel.connected.observe(viewLifecycleOwner, connectObserver)

        setUpNavigationButtons()

        checkIfBluetoothLESupported()
        // If not connected to bluetooth, try to connect
        if (!Globals.bluetoothConnectedStatus && !Globals.bluetoothDiscoveringStatus) {
            PermissionHandler.handleBluetoothPermissionStatus {
                Globals.bluetoothViewModel.tryToConnectBluetoothToArduino()
            }
        }

        bt_connection.setOnClickListener {
            if (!Globals.bluetoothConnectedStatus && !Globals.bluetoothDiscoveringStatus) {
                Globals.bluetoothViewModel.tryToConnectBluetoothToArduino()
            }
        }

        forward_button.setOnTouchListener { _, event ->
            Globals.bluetoothViewModel.setDirectionOnAction(event.action, "Forward")
            true
        }

        reverse_button.setOnTouchListener { _, event ->
            Globals.bluetoothViewModel.setDirectionOnAction(event.action, "Reverse")
            true
        }

        left_button.setOnTouchListener { _, event ->
            Globals.bluetoothViewModel.setDirectionOnAction(event.action, "Left")
            true
        }

        right_button.setOnTouchListener { _, event ->
            Globals.bluetoothViewModel.setDirectionOnAction(event.action, "Right")
            true
        }
    }

    override fun onResume() {
        super.onResume()

        checkIfBluetoothLESupported()

        // If not connected to bluetooth, try to connect
        if (!Globals.bluetoothConnectedStatus && !Globals.bluetoothDiscoveringStatus) {
            PermissionHandler.handleBluetoothPermissionStatus {
                Globals.bluetoothViewModel.tryToConnectBluetoothToArduino()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (Globals.bluetoothConnectedStatus) {
            BluetoothConnectionHandler.initiateAutoMowerControl()
        }
    }

    private fun checkIfBluetoothLESupported() {
        if (!Globals.currentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(
                Globals.currentActivity,
                getString(R.string.bluetooth_le_not_supported),
                Toast.LENGTH_SHORT
            ).show()
            parentFragmentManager.popBackStack()
        }
    }


    private fun setUpNavigationButtons() {
        auto_button.setOnClickListener {
            val direction = ManualFragmentDirections.actionManualFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }
    }


    private fun changeBluetoothConnectionUi(couldConnect: Boolean? = null) {
        if (anim_bt != null && anim_bt.isAnimating) {
            anim_bt.visibility = View.GONE
            anim_bt.cancelAnimation()
            if (couldConnect!!) {
                bt_indicator.visibility = View.VISIBLE
                bt_indicator.setImageResource(R.drawable.ic_bluetooth_connected)
            } else {
                bt_indicator.visibility = View.VISIBLE
                bt_indicator.setImageResource(R.drawable.ic_bluetooth_disconnected)
            }
        } else if (anim_bt != null) {
            bt_indicator.visibility = View.GONE
            anim_bt.visibility = View.VISIBLE
            anim_bt.playAnimation()
        }
    }
}