package com.example.conanmoverandroidapp

import android.bluetooth.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import kotlinx.coroutines.runBlocking


class BluetoothConnectionHandler {
    companion object {
        lateinit var bluetoothGatt: BluetoothGatt

        fun connectToDevice(device: BluetoothDevice, wasSuccessful: (success: Boolean) -> Unit){
            Thread {
                try {
                    Globals.btAdapter?.cancelDiscovery()

                    bluetoothGatt = device.connectGatt(Globals.currentActivity, false, gattCallback)

                    runBlocking {
                        bluetoothGatt.discoverServices()
                    }

                    executeOnMainThread {
                        Globals.bluetoothConnectedStatus = true
                        wasSuccessful(true)
                    }

                    initiateHearbeatToArduino(true)
                } catch (e: Exception){
                    executeOnMainThread {
                        Globals.bluetoothConnectedStatus = false
                        wasSuccessful(false)
                    }
                }
            }.start()
        }

        private val gattCallback = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Globals.bluetoothConnectedStatus = true
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        // TODO: Handle disconnected from mower
                        Globals.bluetoothConnectedStatus = false
                        gatt.close()
                    }
                } else {
                    // TODO: Handle bluetooth error
                    Globals.bluetoothConnectedStatus = false
                    gatt.close()
                }
            }
        }

        fun writeCharacteristic(value: ByteArray?) {
            val service: BluetoothGattService = bluetoothGatt.getService(Globals.arduinoServiceUUID)
            if (service == null) {
                return
            }
            val characteristic = service.getCharacteristic(Globals.arduinoWriteCharacteristicsUUID)
            if (characteristic == null) {
                return
            }
            characteristic.value = value
            val writeStatus: Boolean = bluetoothGatt.writeCharacteristic(characteristic)
            if(!writeStatus){
                // TODO: Handle communication error
            }
        }

        fun directionButtonPressed(direction: String){
            val buffer = ByteArray(2)
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

        fun initiateManualMowerControl(){
            val buffer = ByteArray(2)
            buffer[0] = 77.toByte()
            buffer[1] = 83.toByte()
            writeCharacteristic(buffer)
        }

        fun initiateAutoMowerControl(){
            val buffer = ByteArray(1)
            buffer[0] = 79.toByte()
            writeCharacteristic(buffer)
        }

        fun connectToArduino(onTimeout: () -> Unit){
            // Get all previously paired devices
            val pairedDevices: Set<BluetoothDevice>? = Globals.btAdapter?.bondedDevices
            var foundArduino = false

            // If arduino has been paired before, try to connect
            pairedDevices?.forEach { device ->
                if(device.address == Globals.arduinoMAC){
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

        fun startDiscovery(callback: () -> Unit){
            Globals.btAdapter?.startDiscovery()
            Handler(Looper.getMainLooper()).postDelayed({
                if (!Globals.bluetoothConnectedStatus) {
                    Globals.btAdapter?.cancelDiscovery()
                    callback()
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
        fun initiateHearbeatToArduino(first: Boolean){
            if(first){
                sendHeartbeatToArduino()
            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if (Globals.bluetoothConnectedStatus) {
                    sendHeartbeatToArduino()
                }
            }, 5000)
        }

        fun sendHeartbeatToArduino(){
            if (Globals.bluetoothConnectedStatus) {
                val buffer = ByteArray(1)
                buffer[0] = 100.toByte()
                writeCharacteristic(buffer)
            }
        }
    }

}