package com.weatherwise.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weatherwise.app.data.local.db.dao.WeatherDao
import com.weatherwise.app.data.local.db.entity.CurrentWeatherEntity
import com.weatherwise.app.data.local.db.entity.ForecastEntity

@Database(
    entities = [CurrentWeatherEntity::class, ForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
