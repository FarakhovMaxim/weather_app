package com.example.weather_app.room

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: WeatherDatabase? = null

    fun getDatabase(context: Context): WeatherDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "weather_database"
            ).build()
        }
        return database!!
    }
}
