package com.example.myapplication.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repository: WaterRepository,
    private val userId: String
) : ViewModel() {

    private val _weekData = MutableStateFlow<List<Float>>(emptyList())
    val weekData: StateFlow<List<Float>> = _weekData

    init {
        loadWeekData()
    }

    private fun loadWeekData() {
        viewModelScope.launch {
            val data = repository.getLast7DaysData(userId)
            _weekData.value = data
        }
    }
}
