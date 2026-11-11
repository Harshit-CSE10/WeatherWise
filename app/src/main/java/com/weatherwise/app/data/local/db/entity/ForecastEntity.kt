package com.weatherwise.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey val id: Int = 1,
    val cityName: String,
    val forecastJson: String,
    val cachedAt: Long = System.currentTimeMillis()
)
