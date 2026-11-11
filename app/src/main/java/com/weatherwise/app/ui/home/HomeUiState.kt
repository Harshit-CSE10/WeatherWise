package com.weatherwise.app.ui.home

import com.weatherwise.app.domain.model.CurrentWeather

data class HomeUiState(
    val isLoading: Boolean = false,
    val weather: CurrentWeather? = null,
    val isCacheOutdated: Boolean = false,
    val error: String? = null,
    val lat: Double = 51.5074,
    val lon: Double = -0.1278
)
