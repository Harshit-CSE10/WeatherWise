package com.weatherwise.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.weatherwise.app.data.local.db.entity.CurrentWeatherEntity
import com.weatherwise.app.data.local.db.entity.ForecastEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentWeatherEntity)

    @Query("SELECT * FROM current_weather WHERE id = 1")
    suspend fun getCurrentWeather(): CurrentWeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: ForecastEntity)

    @Query("SELECT * FROM forecast WHERE id = 1")
    suspend fun getForecast(): ForecastEntity?

    @Query("DELETE FROM current_weather")
    suspend fun deleteCurrentWeather()

    @Query("DELETE FROM forecast")
    suspend fun deleteForecast()
}
