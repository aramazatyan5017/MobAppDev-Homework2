package com.example.mobapphomework2_aramazatyan

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val cityList: MutableList<String> = mutableListOf("Yerevan", "Moscow", "Rome", "Berlin")
val apiKey: String = "835accff432e440da92113836231211"
val logTxt: String = "Weather App: "

class MainActivity : ComponentActivity() {

    private val locationPermissionRequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface (
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                requestLocationPermission()
                LocationProvider.initialize(this)
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "screen1"
                ) {
                    val viewModel = Screen1ViewModel()
                    viewModel.getCurrentWeather(this@MainActivity,
                        weatherService = WeatherRetrofitClient.weatherService,
                        apiKey = apiKey)
                    composable("screen1") {
                        Screen1(navController = navController, viewModel = viewModel)
                    }
                    composable("screen2") {
                        Screen2(navController = navController, viewModel = viewModel())
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionRequestCode
            )
        }
    }
}

@Composable
fun Screen1(navController: NavHostController, viewModel: Screen1ViewModel) {
    val weather by viewModel.currentWeather.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome! In order to access the second screen,\n Click the Button!",
            textAlign = TextAlign.Center,
            color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(route = "Screen2") }) {
            Text("Click Me", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        weather?.let {
            Text(text = "In your current location temperature is: ${it.current.temperatureCelsius}" + "°C",
                textAlign = TextAlign.Center,
                color = Color.Gray)
        }
    }
}

@Composable
fun Screen2(navController: NavHostController, viewModel: WeatherViewModel) {
    val weatherList by viewModel.weatherList.observeAsState(emptyList())

    if (weatherList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading cities...",
                style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ))
        }
    } else {
        CompleteList(weatherList = weatherList, navController = navController)
    }
}

@Composable
fun CompleteList(weatherList: List<Weather>, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val localCityList = listOf(
            City(cityList[0], "The Center of the World!", R.drawable.yerevan, weatherList[0].current.temperatureCelsius),
            City(cityList[1], "Russia's capital city", R.drawable.moscow, weatherList[1].current.temperatureCelsius),
            City(cityList[2], "Gigantic Architecture", R.drawable.rome, weatherList[2].current.temperatureCelsius),
            City(cityList[3], "The Berlin Wall was There", R.drawable.berlin, weatherList[3].current.temperatureCelsius),
        )

        LazyColumn {
            items(localCityList) { city ->
                CityListItem(city = city)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate(route = "Screen1") }) {
            Text("Back", color = Color.White)
        }
    }
}

@Composable
fun CityListItem(city: City) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Column (modifier = Modifier.weight(1f)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = city.name,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = city.description + "",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column {
            Image(
                painter = painterResource(id = city.imageId),
                contentDescription = "The Picture of the City!",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Text(
                text = city.temperature.toString() + "°C",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

object WeatherRetrofitClient {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}

typealias LocationCoordinate = Pair<Double, Double>

object LocationProvider {
    private var currentLocation: LocationCoordinate? = null

    fun getCurrentLocation(): LocationCoordinate? {
        return currentLocation
    }

    @SuppressLint("MissingPermission")
    fun initialize(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Request location updates
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1f
            ) { location ->
                location.longitude = 180 + location.longitude
                currentLocation = location.longitude to location.latitude
            }
        }
    }
}

interface WeatherService {
    @GET("current.json")
    suspend fun getWeatherList(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Weather
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