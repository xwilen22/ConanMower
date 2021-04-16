package com.example.conanmoverandroidapp.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.conanmoverandroidapp.*
import kotlinx.android.synthetic.main.auto_fragment.*
import kotlinx.android.synthetic.main.auto_fragment.auto_button
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManualViewModel::class.java)
        // TODO: Use the ViewModel
        setUpNavigationButtons()

        // Connect to bluetooth
        connectBluetoothBtn.setOnClickListener {
            it.isClickable = !BluetoothConnectionHandler.connected

            PermissionHandler.handleBluetoothPermissionStatus {
                tryToConnect(true)
            }
        }

    }

    private fun setUpNavigationButtons()
    {

        auto_button.setOnClickListener {
            val direction = ManualFragmentDirections.actionManualFragmentToAutoFragment()
            it.findNavController().navigate(direction)
        }

    }

    fun tryToConnect(changeLayout: Boolean) {
        if (changeLayout) {
            // TODO
        }

        val deviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, deviceFilter)
        val btstateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, btstateFilter)

        BluetoothConnectionHandler.connectToArduino {
            BluetoothConnectionHandler.executeOnMainThread {
                Toast.makeText(
                    Globals.currentActivity,
                    "Unable to connect to mover.",
                    Toast.LENGTH_SHORT
                ).show()
                if (changeLayout) {
                    // TODO
                }
            }
        }
    }

}