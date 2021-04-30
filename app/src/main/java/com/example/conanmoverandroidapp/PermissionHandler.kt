package com.example.conanmoverandroidapp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog


class PermissionHandler {
    companion object {
        const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_ENABLE_LOCATION = 2
        private var afterPermissionsAllowed: (() -> Unit)? = null

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == REQUEST_ENABLE_BT) {
                if (resultCode == Activity.RESULT_CANCELED) {
                    AlertDialog.Builder(Globals.currentActivity)
                        .setCancelable(false)
                        .setMessage(Globals.currentActivity.getString(R.string.bluetooth_permission_message))
                        .setPositiveButton(Globals.currentActivity.getString(R.string.enable_bluetooth)) { _, _ ->
                            requestBluetoothTurnOn()
                        }
                        .setNegativeButton(Globals.currentActivity.getString(R.string.cancel)) { _, _ -> }
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
            when(requestCode) {
                REQUEST_ENABLE_LOCATION -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (Globals.currentActivity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (Globals.btAdapter?.isEnabled == false) {
                                requestBluetoothTurnOn()
                            }
                            else {
                                if(afterPermissionsAllowed != null){
                                    afterPermissionsAllowed!!()
                                    afterPermissionsAllowed = null
                                }
                            }
                        }
                    }
                    else {
                        AlertDialog.Builder(Globals.currentActivity)
                            .setCancelable(false)
                            .setMessage(Globals.currentActivity.getString(R.string.get_location_access_permission))
                            .setPositiveButton(Globals.currentActivity.getString(R.string.allow_location_access)) { _, _ ->
                                requestLocationAccess()
                            }
                            .setNegativeButton(Globals.currentActivity.getString(R.string.cancel)) { _, _ -> }
                            .show()
                    }
                    return
                }
            }
        }

        fun handleBluetoothPermissionStatus(callback: () -> Unit){
            afterPermissionsAllowed = callback
            if (Globals.btAdapter == null) {
                noBluetoothAdapterAlert()
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

        fun noBluetoothAdapterAlert() {
            AlertDialog.Builder(Globals.currentActivity)
                .setCancelable(false)
                .setMessage(Globals.currentActivity.getString(R.string.no_adapter_error))
                .setPositiveButton(Globals.currentActivity.getString(R.string.ok)) { _, _ -> }.show()
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