package com.example.conanmoverandroidapp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog

class PermissionHandler {
    companion object {
        val REQUEST_ENABLE_BT = 1
        val REQUEST_ENABLE_LOCATION = 2
        var afterPermissionsAllowed: (() -> Unit)? = null

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_CANCELED) {
                    AlertDialog.Builder(Globals.currentActivity)
                        .setCancelable(false)
                        .setMessage("Bluetooth permission message")
                        .setPositiveButton("Enable bluetooth") { _, _ ->
                            requestBluetoothTurnOn()
                        }
                        .setNegativeButton("Cancel") { _, _ -> }
                        .show()
                }
                else {
                    if (Globals.currentActivity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                        requestLocationAccess()
                    }
                    else {
                        if(afterPermissionsAllowed != null){
                            afterPermissionsAllowed!!()
                            afterPermissionsAllowed = null
                        }
                    }
                }
            }
        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            when (requestCode) {
                REQUEST_ENABLE_LOCATION -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (Globals.currentActivity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (Globals.btAdapter?.isEnabled == false) {
                                requestBluetoothTurnOn()
                            } else {
                                if(afterPermissionsAllowed != null){
                                    afterPermissionsAllowed!!()
                                    afterPermissionsAllowed = null
                                }
                            }
                        }
                    } else {
                        AlertDialog.Builder(Globals.currentActivity)
                            .setCancelable(false)
                            .setMessage("Get location access permission")
                            .setPositiveButton("Allow location access") { _, _ ->
                                requestLocationAccess()
                            }
                            .setNegativeButton("Cancel") { _, _ -> }
                            .show()
                    }
                    return
                }
            }
        }

        fun HandleBluetoothPermissionStatus(callback: () -> Unit){
            afterPermissionsAllowed = callback
            if (Globals.btAdapter == null) {
                showNoBtAdapterAlert()
            }
            else if (Globals.btAdapter?.isEnabled == false) {
                requestBluetoothTurnOn()
            }
            else {
                if (Globals.currentActivity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    requestLocationAccess()
                }
                else {
                    callback()
                    afterPermissionsAllowed = null
                }
            }
        }


        fun showNoBtAdapterAlert() {
            AlertDialog.Builder(Globals.currentActivity)
                .setCancelable(false)
                .setMessage("No adapter error")
                .setPositiveButton("OK") { _, _ -> }.show()
        }

        fun requestBluetoothTurnOn() {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            Globals.currentActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        fun requestLocationAccess() {
            Globals.currentActivity.requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ENABLE_LOCATION
            )
        }
    }
}