package com.example.mobapphomework2_aramazatyan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController, settingsViewModel: ScreenSettingsViewModel) {
    val temperatureUnit by settingsViewModel.unit.observeAsState()

    IconButton(
        onClick = {
            navController.popBackStack()
        }
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back to Welcome",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Select Temperature Measurement Unit",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            RadioButton(
                selected = temperatureUnit == TemperatureUnit.CELSIUS,
                onClick = { settingsViewModel.setTemperatureUnit(TemperatureUnit.CELSIUS) },
                modifier = Modifier.scale(1.5f)
            )
            Text("\nCelsius (°C)      ")
        }

        Row {
            RadioButton(
                selected = temperatureUnit == TemperatureUnit.FAHRENHEIT,
                onClick = { settingsViewModel.setTemperatureUnit(TemperatureUnit.FAHRENHEIT) },
                modifier = Modifier.scale(1.5f)
            )
            Text("\nFahrenheit (°F)")
        }
    }
}

