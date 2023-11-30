package com.example.mobapphomework2_aramazatyan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework4()
        }
    }

    @Composable
    fun Homework4() {

        val navController = rememberNavController()

        val settingsViewModel: ScreenSettingsViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = "screen1"
        ) {
            composable("screen1") {
                ScreenOne(navController, settingsViewModel)
            }
            composable("screen2") {
                ScreenTwo(navController)
            }
            composable("cityDetails/{city}") {
                    backStackEntry ->
                val city = backStackEntry.arguments?.getString("city")
                val viewModelFactory = WeatherApiViewModelFactory(city = city.toString())
                val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[Screen2ViewModel::class.java]
                if (city != null) {
                    CityList(city = city, navController, viewModel, settingsViewModel)
                }
            }
            composable("settings") {
                SettingsScreen(navController, settingsViewModel)
            }
        }
    }
}
object WeatherRetrofitClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                            .header("key", API_KEY)
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
    }

    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}

interface WeatherService {
    @GET("current.json")
    suspend fun getWeather(@Query("q") city: String): Weather
}

enum class TemperatureUnit {
    CELSIUS, FAHRENHEIT
}

data class City(
    val name: String,
    val description: String,
    val imageId: Int,
    val temperature: Double
)

data class Weather(
    @SerializedName(value = "current")
    val current: Current
)

data class Current(
    @SerializedName(value = "temp_c")
    val temperatureCelsius: Double,

    @SerializedName(value = "temp_f")
    val temperatureFahrenheit: Double,
)