package com.example.conanmoverandroidapp

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.CountDownTimer
import java.util.*
import kotlin.properties.Delegates

class Globals: Application() {
    companion object {
        var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var btReceiver: BluetoothReceiver = BluetoothReceiver()
        val arduinoMAC = "98:D3:11:F8:6A:DA"
        val arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        lateinit var appContext: Context
        lateinit var currentActivity: Activity
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

}