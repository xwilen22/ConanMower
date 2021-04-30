package com.example.conanmoverandroidapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.view.MotionEvent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.conanmoverandroidapp.BluetoothConnectionHandler
import com.example.conanmoverandroidapp.Globals
import com.example.conanmoverandroidapp.R

class BluetoothViewModel : ViewModel() {

    val connected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun tryToConnectBluetoothToArduino() {
        val deviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, deviceFilter)
        val btStateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        Globals.currentActivity.registerReceiver(Globals.btReceiver, btStateFilter)

        BluetoothConnectionHandler.initiateBluetoothConnectionToArduino { success ->
            connected.value = success
        }
    }

    fun setDirectionOnAction(action: Int, direction: String) {
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
                R.string.no_bluetooth_connection,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    // TODO: Implement the ViewModel
}