package com.example.weather_app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weather_app.models.FavoriteViewModel

@Composable
fun FavoriteScreen(navController: NavHostController, viewModel: FavoriteViewModel) {
    val cities by viewModel.favoriteCities.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Favorite Cities", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        cities.forEach { city ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = city.name, style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { viewModel.removeCity(city.name) }) {
                    Text("Remove")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}