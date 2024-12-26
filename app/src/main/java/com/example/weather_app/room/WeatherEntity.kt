package com.example.weather_app.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city: String,
    val temperature: Int,
    val condition: String,
    val imageResource: Int
)
