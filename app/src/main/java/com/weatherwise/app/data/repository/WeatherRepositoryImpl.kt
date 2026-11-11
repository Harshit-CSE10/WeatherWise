package com.weatherwise.app.data.repository

import com.squareup.moshi.Moshi
import com.weatherwise.app.BuildConfig
import com.weatherwise.app.data.local.db.dao.WeatherDao
import com.weatherwise.app.data.local.db.entity.ForecastEntity
import com.weatherwise.app.data.mapper.ForecastMapper
import com.weatherwise.app.data.mapper.WeatherMapper
import com.weatherwise.app.data.remote.api.WeatherApiService
import com.weatherwise.app.data.remote.dto.ForecastDto
import com.weatherwise.app.domain.model.CurrentWeather
import com.weatherwise.app.domain.model.ForecastData
import com.weatherwise.app.domain.repository.WeatherRepository
import com.weatherwise.app.util.Constants
import com.weatherwise.app.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApiService,
    private val weatherDao: WeatherDao,
    private val moshi: Moshi,
    @Named("api_key") private val apiKey: String = BuildConfig.OPENWEATHER_API_KEY
) : WeatherRepository {

    private var lastFetchTime: Long = 0L

    override fun getCurrentWeather(lat: Double, lon: Double): Flow<Result<CurrentWeather>> = flow {
        emit(Result.Loading)
        val apiKeyMissing = isApiKeyMissing()
        try {
            if (apiKeyMissing) {
                throw IllegalStateException("API key is missing or not configured. Please set OPENWEATHER_API_KEY in local.properties.")
            }
            val dto = weatherApi.getCurrentWeather(lat, lon)
            val weather = WeatherMapper.dtoToDomain(dto)
            val entity = WeatherMapper.domainToEntity(weather)
            weatherDao.insertCurrentWeather(entity)
            lastFetchTime = System.currentTimeMillis()
            emit(Result.Success(weather))
        } catch (e: Exception) {
            val cached = weatherDao.getCurrentWeather()
            if (cached != null) {
                emit(Result.Success(WeatherMapper.entityToDomain(cached)))
            } else {
                val errorMsg = if (apiKeyMissing) {
                    "API key is missing or not configured. Please add OPENWEATHER_API_KEY to local.properties."
                } else {
                    e.message ?: "Failed to fetch weather data"
                }
                emit(Result.Error(errorMsg, e))
            }
        }
    }

    override fun getForecast(lat: Double, lon: Double): Flow<Result<ForecastData>> = flow {
        emit(Result.Loading)
        val apiKeyMissing = isApiKeyMissing()
        val forecastDtoAdapter = moshi.adapter(ForecastDto::class.java)
        try {
            if (apiKeyMissing) {
                throw IllegalStateException("API key is missing or not configured. Please set OPENWEATHER_API_KEY in local.properties.")
            }
            val dto = weatherApi.getForecast(lat, lon)
            val forecastData = ForecastMapper.dtoToDomain(dto)
            val json = forecastDtoAdapter.toJson(dto)
            weatherDao.insertForecast(
                ForecastEntity(
                    cityName = forecastData.cityName,
                    forecastJson = json
                )
            )
            lastFetchTime = System.currentTimeMillis()
            emit(Result.Success(forecastData))
        } catch (e: Exception) {
            val cachedEntity = weatherDao.getForecast()
            if (cachedEntity != null) {
                try {
                    val cachedDto = forecastDtoAdapter.fromJson(cachedEntity.forecastJson)
                    if (cachedDto != null) {
                        emit(Result.Success(ForecastMapper.dtoToDomain(cachedDto)))
                        return@flow
                    }
                } catch (_: Exception) {
                }
            }
            val errorMsg = if (apiKeyMissing) {
                "API key is missing or not configured. Please add OPENWEATHER_API_KEY to local.properties."
            } else {
                e.message ?: "Failed to fetch forecast data"
            }
            emit(Result.Error(errorMsg, e))
        }
    }

    override fun isCacheOutdated(): Boolean {
        if (lastFetchTime == 0L) return true
        return (System.currentTimeMillis() - lastFetchTime) > Constants.CACHE_VALIDITY_MS
    }

    private fun isApiKeyMissing(): Boolean {
        return apiKey.isBlank() || apiKey == "YOUR_KEY_HERE"
    }
}
