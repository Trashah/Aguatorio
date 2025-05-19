package com.example.myapplication.utils

import android.content.Context
import androidx.work.*
import com.example.myapplication.HydrationReminderWorker
import java.util.concurrent.TimeUnit

object NotificationUtils {
    fun scheduleNotifications(context: Context) {
        // Verificar si las notificaciones están habilitadas
        val sharedPrefs = context.getSharedPreferences("aguatorio_prefs", Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPrefs.getBoolean("notifications_enabled", true)

        if (!notificationsEnabled) {
            return // No programar notificaciones si están deshabilitadas
        }

        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<HydrationReminderWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        // Cancelamos trabajos previos para evitar duplicaciones
        workManager.cancelUniqueWork("HydrationReminder")

        // Programamos el nuevo trabajo usando UPDATE en lugar de REPLACE
        workManager.enqueueUniquePeriodicWork(
            "HydrationReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        // Forzamos una notificación inicial para comprobar que funciona
        val immediateWorkRequest = OneTimeWorkRequestBuilder<HydrationReminderWorker>().build()
        workManager.enqueue(immediateWorkRequest)
    }
} 