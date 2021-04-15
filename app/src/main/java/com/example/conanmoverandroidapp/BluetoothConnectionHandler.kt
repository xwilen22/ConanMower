package com.example.conanmoverandroidapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import kotlin.properties.Delegates

class BluetoothConnectionHandler() {
    companion object {
        lateinit var socket: BluetoothSocket
        var outputStream: OutputStream? = null
        var inputStream: InputStream? = null
        var shouldListen = false
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
        var onMessage = mutableMapOf<Int, (UByteArray) -> Unit>()

        fun connectToDevice(device: BluetoothDevice, wasSuccessful: (success: Boolean) -> Unit){
            Thread {
                try {
                    socket = device.createRfcommSocketToServiceRecord(Globals.arduinoUUID)
                    socket.connect()
                    outputStream = socket.outputStream
                    inputStream = socket.inputStream
                    executeOnMainThread {
                        connected = true
                        wasSuccessful(true)
                    }
                    sendHeartbeatToArduino(true)
                    listener.run()
                } catch(e: Exception){
                    executeOnMainThread {
                        connected = false
                        wasSuccessful(false)
                    }
                }
            }.start()
        }

        fun listen() {
            val BUFFER_SIZE = 1024
            val buffer = ByteArray(BUFFER_SIZE)

            try {
                var bytesReceived = inputStream!!.read(buffer)
                var i = 0
                while(i < bytesReceived){
                    val type = buffer[i].toInt()
                    if(type == 0){
                        break
                    }
                    else{
                        i++
                        var dataLength = buffer[i].toInt()
                        if(dataLength == 0){
                            val newBuffer = ByteArray(BUFFER_SIZE)
                            val newBytesReceived = inputStream!!.read(newBuffer)

                            dataLength = newBuffer[0].toInt()

                            for(x in 0..(newBytesReceived - 1)){
                                buffer[i + x] = newBuffer[x]
                            }
                        }
                        val data = ByteArray(dataLength)
                        i++
                        var o = 0
                        while(o < dataLength){
                            data[o] = buffer[i]
                            i++
                            o++
                        }
                        onMessageReceived(type, data.toUByteArray())
                    }
                }
            } catch (e: IOException) {
                executeOnMainThread {
                    connected = false
                }
                e.printStackTrace()
                shouldListen = false
            }
        }

        fun send(bytes: UByteArray) {
            if(::socket.isInitialized && outputStream != null && connected){
                outputStream!!.write(bytes.toByteArray())
            }
        }

        fun sendBlaBlaMessageToArduino(){
            val buffer = UByteArray(3)
            buffer[0] = 1.toUByte()
            buffer[1] = 1.toUByte()
            buffer[2] = 1.toUByte()
            send(buffer)
        }


        private fun onMessageReceived(type: Int, buffer: UByteArray){
            onMessage.forEach { entry ->
                if(entry.key == type){
                    executeOnMainThread {
                        entry.value(buffer)
                    }
                }
            }
        }

        var listener = Runnable {
            run {
                shouldListen = true
                while(shouldListen){
                    listen()
                }
            }
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
            Handler().postDelayed(
                {
                    if (!connected) {
                        Globals.btAdapter?.cancelDiscovery()
                        onArduinoNotFound()
                    }
                },
                20000
            )
        }

        fun tryToConnect(changeLayout: Boolean) {
            if (changeLayout) {
                // TODO
            }

            val deviceFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            Globals.currentActivity.registerReceiver(Globals.btReceiver, deviceFilter)
            val btstateFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            Globals.currentActivity.registerReceiver(Globals.btReceiver, btstateFilter)

            connectToArduino {
                executeOnMainThread {
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

        fun executeOnMainThread(function: () -> Unit){
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                function()
            }
        }

        fun onMessage(messageType: Int, listener: (UByteArray) -> Unit){
            onMessage.put(messageType, listener)
        }

        // Every 5 seconds, send a message to arduino
        fun sendHeartbeatToArduino(first: Boolean){
            if(first){
                val buffer = UByteArray(3)
                buffer[0] = 7.toUByte()
                buffer[1] = 1.toUByte()
                buffer[2] = 1.toUByte()
                send(buffer)
            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if(connected){
                    val buffer = UByteArray(3)
                    buffer[0] = 7.toUByte()
                    buffer[1] = 1.toUByte()
                    buffer[2] = 1.toUByte()
                    send(buffer)
                    sendHeartbeatToArduino(false)
                }
            }, 5000)
        }
    }

}