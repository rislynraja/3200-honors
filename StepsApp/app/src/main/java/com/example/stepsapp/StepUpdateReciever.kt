package com.example.stepsapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StepUpdateReceiver : BroadcastReceiver() {
    companion object {
        var onStepUpdate: ((Long) -> Unit)? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val steps = it.getLongExtra("steps", 0L)
            val source = it.getStringExtra("source") ?: "unknown"
            Log.d("StepUpdateReceiver", "Received $steps steps from source: $source")
            onStepUpdate?.invoke(steps)
        }
    }
}
