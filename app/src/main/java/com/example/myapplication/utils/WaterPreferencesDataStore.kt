package com.example.myapplication.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

// ✅ Mover fuera de la clase y especificar el tipo
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "water_prefs")

class WaterPreferencesDataStore(private val context: Context) {

    companion object {
        val TOTAL_AMOUNT_KEY = intPreferencesKey("total_amount")
        val COMPLETED_GLASSES_KEY = intPreferencesKey("completed_glasses")
    }

    val totalAmountFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TOTAL_AMOUNT_KEY] ?: 0
    }

    val completedGlassesFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[COMPLETED_GLASSES_KEY] ?: 0
    }

    suspend fun saveWaterData(totalAmount: Int, completedGlasses: Int) {
        context.dataStore.edit { preferences ->
            preferences[TOTAL_AMOUNT_KEY] = totalAmount
            preferences[COMPLETED_GLASSES_KEY] = completedGlasses
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
