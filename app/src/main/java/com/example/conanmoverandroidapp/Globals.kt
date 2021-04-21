package com.example.conanmoverandroidapp

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import java.util.*


class Globals: Application() {
    companion object {
        var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var btReceiver: BluetoothReceiver = BluetoothReceiver()
        var bluetoothConnectedStatus: Boolean = false

        val arduinoMAC = "00:1B:10:66:46:72"
        val arduinoServiceUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
        val arduinoWriteCharacteristicsUUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb");
        //val arduinoReadCharacteristicsUUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")

        lateinit var appContext: Context
        lateinit var currentActivity: Activity
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}