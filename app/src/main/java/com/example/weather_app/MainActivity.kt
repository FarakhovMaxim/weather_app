package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.weather_app.mainapp.WeatherApp
import com.example.weather_app.models.FavoriteViewModel
import com.example.weather_app.models.FavoriteViewModelFactory
import com.example.weather_app.room.AppDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "favorite_cities_db"
        ).build()
        val cityDao = db.cityDao()
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val favoriteViewModel = ViewModelProvider(
                    this,
                    FavoriteViewModelFactory(cityDao)
                )[FavoriteViewModel::class.java]
                WeatherApp(favoriteViewModel)
            }
        }
    }
}
