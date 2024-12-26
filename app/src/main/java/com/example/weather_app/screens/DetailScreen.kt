package com.example.weather_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weather_app.R
import com.example.weather_app.cities.CityCoordinates.getCoordinates
import com.example.weather_app.room.WeatherEntity
import com.example.weather_app.room.WeatherDatabase
import com.example.weather_app.room.WeatherDao
import getWeather
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(city: String, navController: NavHostController, weatherDatabase: WeatherDatabase) {
    val weatherInfo = remember { mutableStateOf("Loading...") }
    val temperature = remember { mutableStateOf("Temperature: --°C") }
    val condition = remember { mutableStateOf("Condition: Unknown") }
    val imageResource = remember { mutableStateOf(R.drawable.city) }
    val coroutineScope = rememberCoroutineScope()
    val weatherDao: WeatherDao = weatherDatabase.weatherDao()

    LaunchedEffect(city) {
        coroutineScope.launch {
            val weatherData = weatherDao.getWeatherByCity(city)

            if (weatherData != null) {
                weatherInfo.value = "Data loaded from database"
                temperature.value = "Temperature: ${weatherData.temperature}°C"
                condition.value = "Condition: ${weatherData.condition}"
                imageResource.value = weatherData.imageResource
            } else {
                val cityCoordinates = getCoordinates(city)
                val result = cityCoordinates?.let { getWeather(it.first, it.second, "11c608c5-f657-4aff-bf88-e950970cbfba") }
                if (result != null) {
                    weatherInfo.value = "Data loaded"
                    temperature.value = "Temperature: ${result.fact.temp}°C"
                    condition.value = "Condition: ${result.fact.condition}"
                    imageResource.value = getWeatherImage(result.fact.temp)

                    weatherDao.insertWeather(
                        WeatherEntity(
                            city = city,
                            temperature = result.fact.temp,
                            condition = result.fact.condition,
                            imageResource = imageResource.value
                        )
                    )
                } else {
                    weatherInfo.value = "Failed to load data"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigate("main_screen") }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_home_24),
                                contentDescription = "Go Home",
                                tint = Color(0xFFFFFFFF)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Weather App", color = Color.White)
                    }
                },
                actions = {
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF9FD7E1))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "City: $city",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = imageResource.value),
                        contentDescription = "Weather Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = temperature.value,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = condition.value,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getWeatherImage(temp: Int): Int {
    return when {
        temp <= 0 -> {
            val coldImages = listOf(R.drawable.weather_cold_1, R.drawable.weather_cold_2, R.drawable.weather_cold_3)
            coldImages.random()
        }
        temp.toDouble() in 0.1..20.0 -> {
            val mildImages = listOf(R.drawable.weather_mild_1, R.drawable.weather_mild_2, R.drawable.weather_mild_3)
            mildImages.random()
        }
        temp > 20 -> {
            val hotImages = listOf(R.drawable.weather_hot_1, R.drawable.weather_hot_2)
            hotImages.random()
        }
        else -> R.drawable.city
    }
}
