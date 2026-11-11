package com.weatherwise.app.domain.model

data class ForecastData(
    val cityName: String,
    val hourlyForecasts: List<HourlyForecast>,
    val dailyForecasts: List<DailyForecast>
)
