package com.example.conanmoverandroidapp

import android.bluetooth.*
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.widget.Toast
import java.util.*


class BluetoothConnectionHandler {
    companion object {
        const val arduinoMAC = "00:1B:10:66:46:72"

        val arduinoServiceUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb")
        val arduinoWriteCharacteristicsUUID = UUID.fromString("0000ffe3-0000-1000-8000-00805f9b34fb")
        val arduinoReadCharacteristicsUUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb")

        lateinit var bluetoothGatt: BluetoothGatt

        private fun startDiscovery(callback: (success: Boolean) -> Unit){
            Globals.btAdapter?.startDiscovery()
            Handler(Looper.getMainLooper()).postDelayed({
                if (!Globals.bluetoothConnectedStatus) {
                    Globals.btAdapter?.cancelDiscovery()
                    Globals.bluetoothDiscoveringStatus = false

                    callback(false)
                }
            }, 20000)

            callback(true)
        }

        fun initiateBluetoothConnectionToArduino(callback: (success: Boolean) -> Unit){
            // Get all previously paired devices
            val pairedDevices: Set<BluetoothDevice>? = Globals.btAdapter?.bondedDevices
            var deviceFound = false

            // If arduino has been paired before, try to connect
            pairedDevices?.forEach { device ->
                if(device.address == arduinoMAC){
                    deviceFound = true
                    connectBluetoothToArduino(device, callback)
                }
            }

            // If arduino has not been paired before, start discovery
            if(!deviceFound) {
                Globals.bluetoothDiscoveringStatus = true
                startDiscovery(callback)
            }
        }

        fun connectBluetoothToArduino(device: BluetoothDevice, callback: (success: Boolean) -> Unit)  {
            try {
                Globals.btAdapter?.cancelDiscovery()
                Globals.bluetoothDiscoveringStatus = false
                bluetoothGatt = device.connectGatt(Globals.currentActivity, false, gattCallback(callback))
            }
            catch (e: Exception){
                callback(false)
            }
        }

        private fun gattCallback(callback: (success: Boolean) -> Unit) = object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        Globals.bluetoothConnectedStatus = true

                        bluetoothGatt.discoverServices()
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        Globals.bluetoothConnectedStatus = false
                        gatt.close()

                        callback(false)
                    }
                } else {
                    Globals.bluetoothConnectedStatus = false
                    gatt.close()

                    callback(false)
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                super.onServicesDiscovered(gatt, status)
                if(status == BluetoothGatt.GATT_SUCCESS){
                    initiateHearbeatToArduino()
                    callback(true)
                }
                else{
                    Globals.bluetoothConnectedStatus = false
                    gatt.close()
                    callback(false)
                }
            }
        }

        private fun initiateHearbeatToArduino(){
            // Send first heartbeat
            sendHeartbeatToArduino()

            // Create a background thread that has a Looper
            val handlerThread = HandlerThread("HandlerThread")
            handlerThread.start()

            val handler = Handler(handlerThread.looper)
            var runnable: Runnable? = null
            val delay = 3000

            // Every 5 seconds, send a new message to arduino (in a background thread)
            handler .postDelayed(
                Runnable {
                    handler.postDelayed(runnable!!, delay.toLong())
                    if (Globals.bluetoothConnectedStatus) {
                        sendHeartbeatToArduino()
                    } else {
                        handlerThread.quit()
                    }
                }.also { runnable = it }, delay.toLong())
        }

        private fun writeCharacteristic(value: ByteArray?) {
            // Check if services have been registered
            if(bluetoothGatt.services.size > 0){
                val service: BluetoothGattService = bluetoothGatt.getService(arduinoServiceUUID)
                val characteristic =
                    service.getCharacteristic(arduinoWriteCharacteristicsUUID) ?: return
                characteristic.value = value
                val writeStatus: Boolean = bluetoothGatt.writeCharacteristic(characteristic)
                if(!writeStatus){
                    // TODO: Handle communication error
                }
            }
            else{
                // TODO: Handle no services
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

        fun initiateMowerLineFollowControl(){
            val buffer = ByteArray(1)
            buffer[0] = 95.toByte()
            writeCharacteristic(buffer)
        }

        private fun sendHeartbeatToArduino(){
            val buffer = ByteArray(1)
            buffer[0] = 100.toByte()
            writeCharacteristic(buffer)
        }
    }
}