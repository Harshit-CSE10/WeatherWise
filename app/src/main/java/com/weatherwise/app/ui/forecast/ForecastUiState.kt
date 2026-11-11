package com.weatherwise.app.ui.forecast

import com.weatherwise.app.domain.model.ForecastData

data class ForecastUiState(
    val isLoading: Boolean = false,
    val forecastData: ForecastData? = null,
    val error: String? = null,
    val lat: Double = 51.5074,
    val lon: Double = -0.1278
)
