package com.example.conanmoverandroidapp

import android.bluetooth.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates


class BluetoothConnectionHandler() {
    companion object {
        lateinit var bluetoothGatt: BluetoothGatt

        var onConnect = ArrayList<() -> Unit>()
        var onDisconnect = ArrayList<() -> Unit>()
        var connected: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
            if(oldValue != newValue){
                if(newValue){
                    onConnect.forEach {
                        executeOnMainThread {
                            it()
                        }
                    }
                }
                else{
                    onDisconnect.forEach {
                        executeOnMainThread {
                            it()
                        }
                    }
                }
            }
        }

        fun connectToDevice(device: BluetoothDevice, wasSuccessful: (success: Boolean) -> Unit){
            Thread {
                try {
                    Globals.btAdapter?.cancelDiscovery()

                    bluetoothGatt = device.connectGatt(Globals.currentActivity, false, gattCallback)

                    runBlocking {
                        bluetoothGatt.discoverServices()
                    }

                    executeOnMainThread {
                        connected = true
                        wasSuccessful(true)
                    }

                    sendHeartbeatToArduino(true)
                } catch (e: Exception){
                    executeOnMainThread {
                        connected = false
                        wasSuccessful(false)
                    }
                }
            }.start()
        }

        private val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                val deviceAddress = gatt.device.address

                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress")
                        // TODO: Store a reference to BluetoothGatt
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Log.w(
                            "BluetoothGattCallback",
                            "Successfully disconnected from $deviceAddress"
                        )
                        gatt.close()
                    }
                } else {
                    Log.w(
                        "BluetoothGattCallback",
                        "Error $status encountered for $deviceAddress! Disconnecting..."
                    )
                    gatt.close()
                }
            }
        }

        fun writeCharacteristic(value: ByteArray?) {
            val service: BluetoothGattService = bluetoothGatt.getService(Globals.arduinoServiceUUID)
            if (service == null) {
                println("service null")
                return
            }
            val characteristic = service.getCharacteristic(Globals.arduinoWriteCharacteristicsUUID)
            if (characteristic == null) {
                println("characteristic null")
                return
            }
            characteristic.value = value
            val status: Boolean = bluetoothGatt.writeCharacteristic(characteristic)
            println("Write Status: $status")
        }

        fun directionButtonPressed(direction: String){
            val buffer = ByteArray(3)
            buffer[0] = 77.toByte()

            when (direction) {
                "Left" -> {
                    buffer[1] = 76.toByte()
                }
                "Forward" -> {
                    buffer[1] = 70.toByte()
                }
                "Right" -> {
                    buffer[1] = 82.toByte()
                }
                "Reverse" -> {
                    buffer[1] = 66.toByte()
                }
                else -> {
                    buffer[1] = 83.toByte()
                }
            }
            writeCharacteristic(buffer)
        }

        fun connectToArduino(onTimeout: () -> Unit){
            // Get all previously paired devices
            val pairedDevices: Set<BluetoothDevice>? = Globals.btAdapter?.bondedDevices
            var foundArduino = false

            // If arduino has been paired before, try to connect
            pairedDevices?.forEach { device ->
                val macAddress = device.address
                if(macAddress == Globals.arduinoMAC){
                    foundArduino = true
                    connectToDevice(device) { wasSuccessful ->
                        if(!wasSuccessful){
                            startDiscovery {
                                executeOnMainThread(onTimeout)
                            }
                        }
                    }
                }
            }

            // If arduino has not been paired before, start discovery
            if(!foundArduino) {
                startDiscovery {
                    executeOnMainThread(onTimeout)
                }
            }
        }

        fun startDiscovery(onArduinoNotFound: () -> Unit){
            Globals.btAdapter?.startDiscovery()
            Handler(Looper.getMainLooper()).postDelayed({
                if (!connected) {
                    Globals.btAdapter?.cancelDiscovery()
                    onArduinoNotFound()
                }
            }, 2000)
        }

        fun executeOnMainThread(function: () -> Unit){
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                function()
            }
        }

        // Every 5 seconds, send a message to arduino
        fun sendHeartbeatToArduino(first: Boolean){
            if(first){
                val buffer = ByteArray(3)
                buffer[0] = 79.toByte()
                buffer[1] = 1.toByte()
                buffer[2] = 1.toByte()
                writeCharacteristic(buffer)
            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (connected) {
                    val buffer = ByteArray(3)
                    buffer[0] = 79.toByte()
                    buffer[1] = 1.toByte()
                    buffer[2] = 1.toByte()
                    writeCharacteristic(buffer)
                    sendHeartbeatToArduino(false)
                }
            }, 5000)
        }
    }

}