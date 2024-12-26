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
    val cities = listOf("Moscow", "Saint Petersburg", "London", "Paris", "Tokyo")
    val cityImages = listOf(R.drawable.moscow, R.drawable.saintpetersburg, R.drawable.london, R.drawable.paris, R.drawable.tokyo)
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var cityName by remember { mutableStateOf("") }

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
                .padding(16.dp)
        ) {
            Text(
                text = "Most Popular Cities:",
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
                text = "Enter your city name:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val coordinates = findCoordinatesByCity(cityName)
                    if (coordinates != null) {
                        val (lat, lon) = coordinates
                        navController.navigate("detail_screen/$cityName?lat=$lat&lon=$lon")
                        errorMessage = null
                    } else {
                        errorMessage = "City not found"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FD7E1))
            ) {
                Text("Find City")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red
                )
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFF9FD7E1))
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

fun findCoordinatesByCity(city: String): Pair<Double, Double>? {
    return CityCoordinates.cityMap[city]
}
