package com.example.conanmoverandroidapp

import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.conanmoverandroidapp.ui.PathViewModel


class Globals: Application() {
    companion object {
        var btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var btReceiver: BluetoothReceiver = BluetoothReceiver()
        var bluetoothConnectedStatus: Boolean = false
        var bluetoothDiscoveringStatus: Boolean = false
        var traveledPathSessionList = mutableListOf<TraveledPathSession>()

        lateinit var appContext: Context
        lateinit var currentActivity: Activity
        lateinit var bluetoothViewModel: BluetoothViewModel
        lateinit var pathViewModel: PathViewModel
        lateinit var pathLifeCycleOwner: LifecycleOwner

    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}