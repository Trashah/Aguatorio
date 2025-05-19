package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.WaterRepository
import com.example.myapplication.utils.WaterPreferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WaterViewModel(
    private val dataStore: WaterPreferencesDataStore
) : ViewModel() {

    private val repository = WaterRepository()

    val totalAmount: StateFlow<Int> = dataStore.totalAmountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedGlasses: StateFlow<Int> = dataStore.completedGlassesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addWater(increment: Int = 250) {
        viewModelScope.launch {
            val newTotal = totalAmount.value + increment
            val newGlasses = newTotal / 1000
            dataStore.saveWaterData(newTotal, newGlasses)

            // Guardar en Firebase solo si hay usuario autenticado
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                repository.saveDailyWater(userId, newTotal)
            } else {
                // Aquí podrías loggear o manejar el caso si no hay usuario (opcional)
            }
        }
    }
}
