package com.example.weather_app.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_data WHERE city = :city LIMIT 1")
    suspend fun getWeatherByCity(city: String): WeatherEntity?
}
