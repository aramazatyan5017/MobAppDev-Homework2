package com.example.mobapphomework2_aramazatyan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherService = WeatherRetrofitClient.weatherService

    private val _weatherList = MutableLiveData<List<Weather>>()
    val weatherList: LiveData<List<Weather>> get() = _weatherList

    init {
        viewModelScope.launch {
            try {

                val list: MutableList<Weather> = mutableListOf()

                cityList.forEach { city ->
                    list.add(weatherService.getWeatherList(apiKey, city))
                }

                _weatherList.value = list
            } catch (e: Exception) {
                Log.e("WeatherRetrofit", "Error fetching user data", e)
            }
        }
    }
}