package com.example.weather_app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.example.weather_app.cities.CityCoordinates.getCoordinates
import getWeather
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(city: String) {
    val weatherInfo = remember { mutableStateOf("Загрузка...") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(city) {
        coroutineScope.launch {
            val cityCoordinates = getCoordinates(city)
            val result = cityCoordinates?.let { getWeather(it.first, cityCoordinates.second, "11c608c5-f657-4aff-bf88-e950970cbfba") }
            weatherInfo.value = if (result != null) {
                "Температура: ${result.fact.temp}°C\nУсловие: ${result.fact.condition}"
            } else {
                "Не получилось загрузить данные"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Данные о погоде") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Город: $city",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = weatherInfo.value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
