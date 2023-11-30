package com.example.mobapphomework2_aramazatyan

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

@Composable
fun ScreenOne(navController: NavHostController, settingsViewModel: ScreenSettingsViewModel = viewModel()) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }
    var cityInfo by remember { mutableStateOf("") }

    val fusedLocationClient: FusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean -> }

    DisposableEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )

                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            cityName = addresses[0].locality
                            cityInfo =
                                addresses[0].countryName + ", " + addresses[0].adminArea + ", " + cityName
                        }
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        onDispose { }
    }

    val viewModelFactory = WeatherApiViewModelFactory(city = cityName)
    val viewModel = ViewModelProvider(ViewModelStore(), viewModelFactory)[Screen2ViewModel::class.java]
    val weather by viewModel.weather.observeAsState()
    val temp = weather?.current?.temperatureCelsius
    val respectiveTemp = temp?.let { settingsViewModel.convertTemperature(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome!",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Click the button to access the weather information",
            style = TextStyle(fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("screen2")
            }
        ) {
            Text(text = "Weather Info")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (respectiveTemp != null) {
            val text = buildString {
                append("Temperature in Your Current Location: ")
                append(respectiveTemp)
                if (settingsViewModel.unit.value == TemperatureUnit.FAHRENHEIT) {
                    append(" °F")
                } else {
                    append(" °C")
                }
            }
            Text(
                text = text,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("settings")
            }
        ) {
            Text(text = "Settings")
        }
    }
}