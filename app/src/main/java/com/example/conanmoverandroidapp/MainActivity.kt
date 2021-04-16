package com.example.conanmoverandroidapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()
        setContentView(R.layout.activity_main)

        Globals.currentActivity = this

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

    /*fun initiateBluetoothConnection() {
        if (BluetoothConnectionHandler.connected) {
            // Already connected
        } else {
            // Not connected
            PermissionHandler.HandleBluetoothPermissionStatus {
                BluetoothConnectionHandler.tryToConnect(false)
            }
        }
    }*/
}