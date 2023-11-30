package com.example.mobapphomework2_aramazatyan

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CityList(city: String, navController: NavHostController, viewModel: Screen2ViewModel, settingsViewModel: ScreenSettingsViewModel) {
    val images = cityImageMap[city]
    val descriptions = cityDescriptionMap[city]
    val weather by viewModel.weather.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate("screen2")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = city,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (images != null) {
            val imagePainter = painterResource(id = images)
            Image(
                painter = imagePainter,
                contentDescription = "City Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (descriptions != null) {
            Text(
                text = descriptions,
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = "",
                style = TextStyle(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (weather != null) {
            val tempCelsius = weather!!.current.temperatureCelsius
            val convertedTemp = settingsViewModel.convertTemperature(tempCelsius)
            val temperatureText = buildString {
                append("Current Temperature: ")
                append(convertedTemp)
                if (settingsViewModel.unit.value == TemperatureUnit.FAHRENHEIT) {
                    append(" °F")
                } else {
                    append(" °C")
                }
            }

            Text(
                text = temperatureText,
                style = TextStyle(fontSize = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
        }

        BackHandler {
            navController.popBackStack()
        }
    }
}