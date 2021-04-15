package com.example.conanmoverandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Globals.currentActivity = this

        // Connect to bluetooth
        findViewById<Button>(R.id.connectBluetoothBtn).setOnClickListener {
            it.isClickable = !BluetoothConnectionHandler.connected

            initiateBluetoothConnection()
        }

        BluetoothConnectionHandler.onMessage(3) {
            it[0].toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        Globals.currentActivity = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PermissionHandler.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun initiateBluetoothConnection() {
        if (BluetoothConnectionHandler.connected) {
            // Already connected
        } else {
            // Not connected
            PermissionHandler.HandleBluetoothPermissionStatus {
                BluetoothConnectionHandler.tryToConnect(false)
            }
        }
    }
}