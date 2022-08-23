package com.oak.simpleweather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oak.simpleweather.model.Favorite
import com.oak.simpleweather.model.Unit

@Database(entities = [Favorite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}