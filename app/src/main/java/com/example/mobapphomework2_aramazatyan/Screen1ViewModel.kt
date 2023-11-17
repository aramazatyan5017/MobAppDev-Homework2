package com.example.mobapphomework2_aramazatyan

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Screen1ViewModel : ViewModel() {

    private val _currentWeather = MutableLiveData<Weather>()
    val currentWeather: LiveData<Weather> get() = _currentWeather

    fun getCurrentWeather(
        context: Context,
        weatherService: WeatherService,
        apiKey: String
    ) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModelScope.launch {
                    var location = LocationProvider.getCurrentLocation()
                    if (location == null) {
                        delay(5000L)
                        location = LocationProvider.getCurrentLocation()
                    }
                    if (location != null) {
                        val cityName = "${location.second},${location.first}"
                        val weather = weatherService.getWeatherList(apiKey, cityName)
                        _currentWeather.postValue(weather)
                    } else {
                        Log.e(logTxt, "Unable to retrieve location")
                    }
                }
            } else {
                Log.e(logTxt, "Location not granted")
            }
        } catch (e: Exception) {
            Log.e(logTxt, "Error fetching location data", e)
        }
    }
}