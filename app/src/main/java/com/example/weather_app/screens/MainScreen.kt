package com.example.weather_app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import com.example.weather_app.R
import com.example.weather_app.cities.CityCoordinates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val cities = listOf("Moscow", "Saint Petersburg", "London", "Paris")
    val cityImages = listOf(R.drawable.moscow, R.drawable.saintpetersburg, R.drawable.london, R.drawable.paris)

    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var foundCity by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Weather App")
                },
                actions = {
                    IconButton(onClick = { navController.navigate("favorite_screen") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_favorite_24),
                            contentDescription = "Любимые города",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EE))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Наиболее популярные города:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cities.zip(cityImages)) { (city, imageId) ->
                    CityCard(city = city, imageId = imageId, navController = navController)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Введите координаты вашего города:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Широта") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Долгота") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val lat = latitude.toDoubleOrNull()
                    val lon = longitude.toDoubleOrNull()
                    if (lat != null && lon != null) {
                        foundCity = findCityByCoordinates(lat, lon)
                        if (foundCity != null) {
                            navController.navigate("detail_screen/$foundCity")
                        }
                    } else {
                        foundCity = "Неверные координаты"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Найти город")
            }
        }
    }
}

@Composable
fun CityCard(city: String, imageId: Int, navController: NavHostController) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { navController.navigate("detail_screen/$city") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = city,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 8.dp),
                alignment = Alignment.Center
            )
            Text(
                text = city,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun findCityByCoordinates(lat: Double, lon: Double): String? {
    for ((city, coordinates) in CityCoordinates.cityMap) {
        if (coordinates.first == lat && coordinates.second == lon) {
            return city
        }
    }
    return null
}
