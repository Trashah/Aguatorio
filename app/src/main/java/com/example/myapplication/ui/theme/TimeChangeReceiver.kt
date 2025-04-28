package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TimeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_TIME_CHANGED || intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
            Log.d("TimeChangeReceiver", "Cambio de hora detectado, reprogramando notificaciones.")

            // Llamar a la función para reprogramar notificaciones
            scheduleNotifications(context)
        }
    }

    private fun scheduleNotifications(context: Context) {
        // Implementa aquí la lógica para reprogramar las notificaciones.
        Log.d("TimeChangeReceiver", "Notificaciones reprogramadas.")
    }
}
