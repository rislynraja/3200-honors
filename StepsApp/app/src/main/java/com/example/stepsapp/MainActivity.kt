package com.example.stepsapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var stepCountText: TextView
    private lateinit var stepUpdateReceiver: StepUpdateReceiver

    private val permissionLauncher = registerForActivityResult( // need to ask for permission first
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startPassiveService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        stepCountText = TextView(this).apply {
            text = "Steps: 0" // starting text
            textSize = 24f
        }

        setContentView(stepCountText)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            startPassiveService()
        }

        stepUpdateReceiver = StepUpdateReceiver() // receiver instance

        val filter = IntentFilter("STEP_UPDATE") // receiving new steps
        registerReceiver(stepUpdateReceiver, filter, Context.RECEIVER_EXPORTED)

        StepUpdateReceiver.onStepUpdate = { steps ->
            stepCountText.text = "Steps: $steps" // updates the UI
        }

    }

    private fun startPassiveService() {
        val intent = Intent(this, StepListenerService::class.java)
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(stepUpdateReceiver) // unregister receiver
    }
}
