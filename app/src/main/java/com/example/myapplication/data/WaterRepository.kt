package com.example.myapplication.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class WaterRepository {

    private val db = FirebaseFirestore.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getTodayDate(): String = dateFormat.format(Date())

    suspend fun saveDailyWater(userId: String, totalMl: Int) {
        val today = getTodayDate()
        val liters = totalMl / 1000f
        val docRef = db.collection("users")
            .document(userId)
            .collection("consumption")
            .document(today)

        docRef.set(
            mapOf(
                "date" to today,
                "liters" to liters
            )
        ).await()
    }

    suspend fun getLast7DaysData(userId: String): List<Float> {
        val today = Calendar.getInstance()
        val pastWeek = (0..6).map {
            val cal = today.clone() as Calendar
            cal.add(Calendar.DAY_OF_YEAR, -it)
            dateFormat.format(cal.time)
        }

        val snapshot = db.collection("users")
            .document(userId)
            .collection("consumption")
            .whereIn("date", pastWeek)
            .get()
            .await()

        val map = snapshot.documents.associate {
            it.getString("date")!! to (it.getDouble("liters") ?: 0.0).toFloat()
        }

        return pastWeek.reversed().map { map[it] ?: 0f }
    }
}
