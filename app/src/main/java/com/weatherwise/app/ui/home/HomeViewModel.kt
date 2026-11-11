package com.weatherwise.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherwise.app.domain.repository.WeatherRepository
import com.weatherwise.app.util.LocationHelper
import com.weatherwise.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationHelper: LocationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDefaultOrGPSLocation()
    }

    fun loadDefaultOrGPSLocation() {
        viewModelScope.launch {
            if (locationHelper.hasLocationPermission()) {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    fetchWeatherForCoordinates(location.latitude, location.longitude)
                    return@launch
                }
            }
            fetchWeatherForCoordinates(_uiState.value.lat, _uiState.value.lon)
        }
    }

    fun fetchWeatherForCoordinates(lat: Double, lon: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(lat = lat, lon = lon) }
            weatherRepository.getCurrentWeather(lat, lon).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                weather = result.data,
                                isCacheOutdated = weatherRepository.isCacheOutdated(),
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        fetchWeatherForCoordinates(_uiState.value.lat, _uiState.value.lon)
    }
}
