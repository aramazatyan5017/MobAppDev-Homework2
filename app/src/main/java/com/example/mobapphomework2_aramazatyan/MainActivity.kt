package com.example.mobapphomework2_aramazatyan

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobapphomework2_aramazatyan.ui.theme.MobAppHomework2_AramAzatyanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homework2App()
        }
    }
}

data class City(
    val name: String,
    val description: String,
    val imageId: Int
)

@Composable
fun Homework2App() {
    var isCurrentScreen1 by remember { mutableStateOf(true) }

    if (isCurrentScreen1) {
        Screen1 {isCurrentScreen1 = false}
    } else {
        Screen2 {isCurrentScreen1 = true}
    }
}

@Composable
fun Screen1(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome! In order to access the second screen,\n Click the Button!",
            textAlign = TextAlign.Center,
            color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onButtonClick) {
            Text("Click Me", color = Color.White)
        }
    }
}

@Composable
fun Screen2(onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val cityList = listOf(
            City("Yerevan", "The Center of the World!", R.drawable.yerevan),
            City("Moscow", "Russia's capital city", R.drawable.moscow),
            City("Rome", "Gigantic Architecture", R.drawable.rome),
            City("Berlin", "The Berlin Wall was There", R.drawable.berlin),
        )

        LazyColumn {
            items(cityList) { city ->
                CityListItem(city = city)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onButtonClick) {
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
                text = city.description,
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobAppHomework2_AramAzatyanTheme {
        Homework2App()
    }
}