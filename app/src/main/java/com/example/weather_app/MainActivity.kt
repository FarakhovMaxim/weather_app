package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.room.Room
import com.example.weather_app.mainapp.WeatherApp
import com.example.weather_app.room.WeatherDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val weatherDatabase = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java, "weather-database"
        ).build()
        setContent {
            MaterialTheme {
                WeatherApp(weatherDatabase)
            }
        }
    }
}
