package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Recibida alarma, enviando notificación.")
        sendNotification(context)
    }

    private fun sendNotification(context: Context) {
        val builder = NotificationCompat.Builder(context, "aguatorio_channel")
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle("¡Hora de beber agua!")
            .setContentText("Recuerda mantenerte hidratado 💧")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(1, builder.build())
    }
}
