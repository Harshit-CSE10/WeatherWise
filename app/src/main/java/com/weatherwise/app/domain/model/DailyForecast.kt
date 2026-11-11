package com.weatherwise.app.domain.model

data class DailyForecast(
    val date: java.time.LocalDate,
    val tempMin: Double,
    val tempMax: Double,
    val condition: String,
    val conditionIcon: String,
    val humidity: Int,
    val windSpeed: Double,
    val pop: Double,
    val hourlyForecasts: List<HourlyForecast>
)
