package com.example.weather_app.mainapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather_app.room.WeatherDatabase
import com.example.weather_app.screens.DetailScreen
import com.example.weather_app.screens.MainScreen

@Composable
fun WeatherApp(weatherDatabase: WeatherDatabase) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("detail_screen/{city}") { backStackEntry ->
            val city = backStackEntry.arguments?.getString("city") ?: "Unknown City"
            DetailScreen(city, navController, weatherDatabase = weatherDatabase)
        }
    }
}