package com.example.conanmoverandroidapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat


class BluetoothReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(
                BluetoothAdapter.EXTRA_STATE,
                BluetoothAdapter.ERROR
            )
            when (state) {
                BluetoothAdapter.STATE_OFF -> promptUserToEnableBluetooth()
            }
        }
        else if (action == BluetoothDevice.ACTION_FOUND) {
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            if(device != null){
                if(device.address == BluetoothConnectionHandler.arduinoMAC){
                    BluetoothConnectionHandler.connectBluetoothToArduino(device) {}
                }
            }
        }
    }

    private fun promptUserToEnableBluetooth(){
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        ActivityCompat.startActivityForResult(Globals.currentActivity, enableBtIntent, PermissionHandler.REQUEST_ENABLE_BT, null)
    }
}