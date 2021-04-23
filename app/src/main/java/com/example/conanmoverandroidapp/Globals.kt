package com.example.conanmoverandroidapp

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Handler
import android.os.Looper
import java.util.*


class Globals: Application() {
    companion object {
        var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var btReceiver: BluetoothReceiver = BluetoothReceiver()
        var bluetoothConnectedStatus: Boolean = false
        var bluetoothDiscoveringStatus: Boolean = false

        lateinit var appContext: Context
        lateinit var currentActivity: Activity
        lateinit var bluetoothViewModel: BluetoothViewModel

        fun executeOnMainThread(function: () -> Unit){
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                function()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}