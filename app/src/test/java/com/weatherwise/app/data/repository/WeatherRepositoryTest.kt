package com.weatherwise.app.data.repository

import app.cash.turbine.test
import com.squareup.moshi.Moshi
import com.weatherwise.app.data.local.db.dao.WeatherDao
import com.weatherwise.app.data.local.db.entity.CurrentWeatherEntity
import com.weatherwise.app.data.remote.api.WeatherApiService
import com.weatherwise.app.data.remote.dto.*
import com.weatherwise.app.util.Result
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WeatherRepositoryTest {

    private val weatherApi: WeatherApiService = mockk()
    private val weatherDao: WeatherDao = mockk(relaxed = true)
    private val moshi: Moshi = Moshi.Builder().build()
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(weatherApi, weatherDao, moshi, apiKey = "valid_test_api_key")
    }

    @Test
    fun `getCurrentWeather emits Loading then Success when API call succeeds`() = runTest {
        val dto = CurrentWeatherDto(
            coord = CoordDto(lon = 10.0, lat = 50.0),
            weather = listOf(WeatherConditionDto(id = 800, main = "Clear", description = "clear", icon = "01d")),
            main = MainDto(temp = 25.0, feelsLike = 24.0, tempMin = 20.0, tempMax = 28.0, pressure = 1012, humidity = 50),
            visibility = 10000,
            wind = WindDto(speed = 5.0, deg = 120),
            clouds = CloudsDto(all = 0),
            dt = 1700000000L,
            sys = SysDto(country = "US", sunrise = 1699960000L, sunset = 1700000000L),
            timezone = -18000,
            id = 1,
            name = "New York"
        )

        coEvery { weatherApi.getCurrentWeather(50.0, 10.0) } returns dto

        repository.getCurrentWeather(50.0, 10.0).test {
            val loadingItem = awaitItem()
            assertTrue(loadingItem is Result.Loading)

            val successItem = awaitItem()
            assertTrue(successItem is Result.Success)
            val weather = (successItem as Result.Success).data
            assertEquals("New York", weather.cityName)
            assertEquals(25.0, weather.temperature, 0.01)

            awaitComplete()
        }

        coVerify(exactly = 1) { weatherDao.insertCurrentWeather(any()) }
    }

    @Test
    fun `getCurrentWeather falls back to cache when network throws exception`() = runTest {
        val cachedEntity = CurrentWeatherEntity(
            cityName = "Cached City",
            country = "US",
            temperature = 18.0,
            feelsLike = 17.0,
            tempMin = 15.0,
            tempMax = 20.0,
            humidity = 55,
            pressure = 1014,
            windSpeed = 3.5,
            windDeg = 90,
            visibility = 10000,
            cloudiness = 10,
            condition = "Clouds",
            conditionDescription = "partly cloudy",
            conditionIcon = "02d",
            sunrise = 1700000000L,
            sunset = 1700040000L,
            dt = 1700020000L,
            timezone = -18000,
            lat = 50.0,
            lon = 10.0
        )

        coEvery { weatherApi.getCurrentWeather(50.0, 10.0) } throws Exception("Network Unavailable")
        coEvery { weatherDao.getCurrentWeather() } returns cachedEntity

        repository.getCurrentWeather(50.0, 10.0).test {
            val loadingItem = awaitItem()
            assertTrue(loadingItem is Result.Loading)

            val successItem = awaitItem()
            assertTrue(successItem is Result.Success)
            val weather = (successItem as Result.Success).data
            assertEquals("Cached City", weather.cityName)
            assertEquals(18.0, weather.temperature, 0.01)

            awaitComplete()
        }
    }

    @Test
    fun `getCurrentWeather emits Error when network fails and no cache available`() = runTest {
        coEvery { weatherApi.getCurrentWeather(50.0, 10.0) } throws Exception("Server Error")
        coEvery { weatherDao.getCurrentWeather() } returns null

        repository.getCurrentWeather(50.0, 10.0).test {
            val loadingItem = awaitItem()
            assertTrue(loadingItem is Result.Loading)

            val errorItem = awaitItem()
            assertTrue(errorItem is Result.Error)
            assertEquals("Server Error", (errorItem as Result.Error).message)

            awaitComplete()
        }
    }
}
