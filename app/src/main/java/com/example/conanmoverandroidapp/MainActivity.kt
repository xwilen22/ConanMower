package com.example.conanmoverandroidapp

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.auto_fragment.*
import kotlinx.android.synthetic.main.auto_fragment.auto_button
import kotlinx.android.synthetic.main.auto_fragment.manual_button
import kotlinx.android.synthetic.main.manual_fragment.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: BluetoothViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        setContentView(R.layout.activity_main)

        Globals.currentActivity = this
        Globals.bluetoothViewModel = ViewModelProviders.of(this).get(BluetoothViewModel::class.java)

    }


    override fun onResume() {
        super.onResume()
        Globals.currentActivity = this
        Globals.bluetoothViewModel = ViewModelProviders.of(this).get(BluetoothViewModel::class.java)
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

}