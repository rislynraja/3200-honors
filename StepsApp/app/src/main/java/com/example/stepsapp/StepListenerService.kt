package com.example.stepsapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveMonitoringClient
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.PassiveListenerCallback
import androidx.health.services.client.clearPassiveListenerCallback
import androidx.health.services.client.data.DataPointContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StepListenerService : Service() {
    private lateinit var passiveClient: PassiveMonitoringClient
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val callback = object : PassiveListenerCallback {
        override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
            val stepPoints = dataPoints.getData(DataType.STEPS)

            if (stepPoints.isNotEmpty()) {
                val latestSteps = stepPoints.last().value

                Log.d("StepListenerService", "Steps: $latestSteps")

                val intent = Intent("STEP_UPDATE").apply { // sending new steps (also helpful for testing)
                    putExtra("steps", latestSteps)
                    putExtra("source", "health_service")
                }
                sendBroadcast(intent)

            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        passiveClient = HealthServices.getClient(this).passiveMonitoringClient

        val config = PassiveListenerConfig.Builder()
            .setDataTypes(setOf(DataType.STEPS))
            .build()

        serviceScope.launch {
            try {
                passiveClient.setPassiveListenerCallback(config, callback)
                Log.d("StepListenerService", "step listener registered")
            } catch (e: Exception) {
                Log.e("StepListenerService", "Failed to register", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.launch {
            try {
                passiveClient.clearPassiveListenerCallback()
                Log.d("StepListenerService", "Step listener cleared")
            } catch (e: Exception) {
                Log.e("StepListenerService", "Error clearing", e)
            }
        }
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
