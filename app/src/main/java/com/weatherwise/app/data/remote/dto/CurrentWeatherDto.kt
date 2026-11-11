package com.weatherwise.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @Json(name = "coord") val coord: CoordDto,
    @Json(name = "weather") val weather: List<WeatherConditionDto>,
    @Json(name = "main") val main: MainDto,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "wind") val wind: WindDto,
    @Json(name = "clouds") val clouds: CloudsDto,
    @Json(name = "dt") val dt: Long,
    @Json(name = "sys") val sys: SysDto,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class CoordDto(
    @Json(name = "lon") val lon: Double,
    @Json(name = "lat") val lat: Double
)

@JsonClass(generateAdapter = true)
data class WeatherConditionDto(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
)

@JsonClass(generateAdapter = true)
data class MainDto(
    @Json(name = "temp") val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int
)

@JsonClass(generateAdapter = true)
data class WindDto(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val deg: Int
)

@JsonClass(generateAdapter = true)
data class CloudsDto(
    @Json(name = "all") val all: Int
)

@JsonClass(generateAdapter = true)
data class SysDto(
    @Json(name = "country") val country: String?,
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long
)
