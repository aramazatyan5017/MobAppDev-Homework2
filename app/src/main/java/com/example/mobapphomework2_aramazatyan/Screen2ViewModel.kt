package com.example.mobapphomework2_aramazatyan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class Screen2ViewModel(private val city: String) : ViewModel() {
    private val weatherService = WeatherRetrofitClient.weatherService

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> get() = _weather

    init {
        viewModelScope.launch {
            try {
                val response = weatherService.getWeather(city)
                _weather.value = response
            } catch (e: Exception) {
                Log.e("Retrofit", "Error fetching user data", e)
            }
        }
    }
}

class WeatherApiViewModelFactory(private val city: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Screen2ViewModel::class.java)) {
            return Screen2ViewModel(city) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}