package com.weatherwise.app.domain.model

data class HourlyForecast(
    val dt: Long,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val condition: String,
    val conditionDescription: String,
    val conditionIcon: String,
    val pop: Double // probability of precipitation
)
