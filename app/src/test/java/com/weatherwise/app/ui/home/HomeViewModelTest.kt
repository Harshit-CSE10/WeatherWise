package com.weatherwise.app.ui.home

import app.cash.turbine.test
import com.weatherwise.app.domain.model.CurrentWeather
import com.weatherwise.app.domain.repository.WeatherRepository
import com.weatherwise.app.util.LocationHelper
import com.weatherwise.app.util.Result
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val weatherRepository: WeatherRepository = mockk(relaxed = true)
    private val locationHelper: LocationHelper = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { locationHelper.hasLocationPermission() } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeatherForCoordinates updates uiState with weather data on success`() = runTest {
        val weather = CurrentWeather(
            cityName = "Berlin",
            country = "DE",
            temperature = 22.0,
            feelsLike = 21.0,
            tempMin = 18.0,
            tempMax = 25.0,
            humidity = 50,
            pressure = 1013,
            windSpeed = 4.0,
            windDeg = 180,
            visibility = 10000,
            cloudiness = 0,
            condition = "Clear",
            conditionDescription = "clear sky",
            conditionIcon = "01d",
            sunrise = 1700000000L,
            sunset = 1700040000L,
            dt = 1700020000L,
            timezone = 3600,
            lat = 52.52,
            lon = 13.405
        )

        coEvery { weatherRepository.getCurrentWeather(52.52, 13.405) } returns flowOf(
            Result.Loading,
            Result.Success(weather)
        )
        every { weatherRepository.isCacheOutdated() } returns false

        viewModel = HomeViewModel(weatherRepository, locationHelper)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.fetchWeatherForCoordinates(52.52, 13.405)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Berlin", state.weather?.cityName)
        assertEquals(22.0, state.weather?.temperature ?: 0.0, 0.01)
    }

    @Test
    fun `fetchWeatherForCoordinates updates uiState with error message on failure`() = runTest {
        coEvery { weatherRepository.getCurrentWeather(10.0, 10.0) } returns flowOf(
            Result.Loading,
            Result.Error("Network Error")
        )

        viewModel = HomeViewModel(weatherRepository, locationHelper)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.fetchWeatherForCoordinates(10.0, 10.0)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network Error", state.error)
    }
}
