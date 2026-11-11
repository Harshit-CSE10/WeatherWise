package com.weatherwise.app.domain.repository

import com.weatherwise.app.domain.model.CurrentWeather
import com.weatherwise.app.domain.model.ForecastData
import com.weatherwise.app.util.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double): Flow<Result<CurrentWeather>>
    fun getForecast(lat: Double, lon: Double): Flow<Result<ForecastData>>
    fun isCacheOutdated(): Boolean
}
