package com.weatherwise.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastDto(
    @Json(name = "cod") val cod: String,
    @Json(name = "message") val message: Int,
    @Json(name = "cnt") val cnt: Int,
    @Json(name = "list") val list: List<ForecastItemDto>,
    @Json(name = "city") val city: ForecastCityDto
)

@JsonClass(generateAdapter = true)
data class ForecastItemDto(
    @Json(name = "dt") val dt: Long,
    @Json(name = "main") val main: MainDto,
    @Json(name = "weather") val weather: List<WeatherConditionDto>,
    @Json(name = "wind") val wind: WindDto,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "pop") val pop: Double,
    @Json(name = "dt_txt") val dtTxt: String
)

@JsonClass(generateAdapter = true)
data class ForecastCityDto(
    @Json(name = "id") val id: Long,
    @Json(name = "name") val name: String,
    @Json(name = "coord") val coord: CoordDto,
    @Json(name = "country") val country: String,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long
)
