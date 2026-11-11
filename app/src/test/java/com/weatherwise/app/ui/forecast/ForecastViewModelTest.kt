package com.weatherwise.app.ui.forecast

import com.weatherwise.app.domain.model.ForecastData
import com.weatherwise.app.domain.repository.WeatherRepository
import com.weatherwise.app.util.Result
import io.mockk.coEvery
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
class ForecastViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val weatherRepository: WeatherRepository = mockk()
    private lateinit var viewModel: ForecastViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastViewModel(weatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchForecast updates uiState with ForecastData on success`() = runTest {
        val forecastData = ForecastData(
            cityName = "Madrid",
            hourlyForecasts = emptyList(),
            dailyForecasts = emptyList()
        )

        coEvery { weatherRepository.getForecast(40.4168, -3.7038) } returns flowOf(
            Result.Loading,
            Result.Success(forecastData)
        )

        viewModel.fetchForecast(40.4168, -3.7038)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Madrid", state.forecastData?.cityName)
    }
}
