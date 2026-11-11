package com.weatherwise.app.domain.model

data class CurrentWeather(
    val cityName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val visibility: Int,
    val cloudiness: Int,
    val condition: String,
    val conditionDescription: String,
    val conditionIcon: String,
    val sunrise: Long,
    val sunset: Long,
    val dt: Long,
    val timezone: Int,
    val lat: Double,
    val lon: Double
)
