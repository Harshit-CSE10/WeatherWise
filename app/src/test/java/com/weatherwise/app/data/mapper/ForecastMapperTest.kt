package com.weatherwise.app.data.mapper

import com.weatherwise.app.data.remote.dto.*
import com.weatherwise.app.domain.model.HourlyForecast
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneOffset

class ForecastMapperTest {

    @Test
    fun `groupByDay aggregates 3-hour forecasts into daily high low correctly`() {
        // Base epoch for day 1 (UTC 2024-01-01T00:00:00Z)
        val baseEpoch = 1704067200L // Jan 1, 2024

        val hourlyList = listOf(
            createHourly(dt = baseEpoch, temp = 10.0, tempMin = 8.0, tempMax = 12.0, condition = "Clouds"),
            createHourly(dt = baseEpoch + 3600 * 3, temp = 15.0, tempMin = 14.0, tempMax = 18.0, condition = "Clear"),
            createHourly(dt = baseEpoch + 3600 * 6, temp = 20.0, tempMin = 18.0, tempMax = 22.0, condition = "Clear"),
            createHourly(dt = baseEpoch + 3600 * 9, temp = 12.0, tempMin = 10.0, tempMax = 14.0, condition = "Clear")
        )

        val dailyList = ForecastMapper.groupByDay(hourlyList, timezoneOffset = 0)

        assertEquals(1, dailyList.size)
        val day1 = dailyList.first()
        assertEquals(8.0, day1.tempMin, 0.01)
        assertEquals(22.0, day1.tempMax, 0.01)
        assertEquals("Clear", day1.condition)
        assertEquals(4, day1.hourlyForecasts.size)
    }

    @Test
    fun `dtoToDomain maps ForecastDto correctly`() {
        val baseEpoch = 1704067200L

        val forecastDto = ForecastDto(
            cod = "200",
            message = 0,
            cnt = 2,
            list = listOf(
                createForecastItemDto(dt = baseEpoch, temp = 15.0, condition = "Rain"),
                createForecastItemDto(dt = baseEpoch + 3600 * 3, temp = 17.0, condition = "Rain")
            ),
            city = ForecastCityDto(
                id = 100,
                name = "Paris",
                coord = CoordDto(lat = 48.8566, lon = 2.3522),
                country = "FR",
                timezone = 3600,
                sunrise = baseEpoch,
                sunset = baseEpoch + 43200
            )
        )

        val domain = ForecastMapper.dtoToDomain(forecastDto)

        assertEquals("Paris", domain.cityName)
        assertEquals(2, domain.hourlyForecasts.size)
        assertEquals(1, domain.dailyForecasts.size)
        assertEquals("Rain", domain.dailyForecasts.first().condition)
    }

    private fun createHourly(
        dt: Long,
        temp: Double,
        tempMin: Double,
        tempMax: Double,
        condition: String
    ): HourlyForecast {
        return HourlyForecast(
            dt = dt,
            temperature = temp,
            feelsLike = temp,
            tempMin = tempMin,
            tempMax = tempMax,
            humidity = 50,
            pressure = 1013,
            windSpeed = 3.0,
            condition = condition,
            conditionDescription = condition.lowercase(),
            conditionIcon = "01d",
            pop = 0.1
        )
    }

    private fun createForecastItemDto(
        dt: Long,
        temp: Double,
        condition: String
    ): ForecastItemDto {
        return ForecastItemDto(
            dt = dt,
            main = MainDto(
                temp = temp,
                feelsLike = temp,
                tempMin = temp - 2,
                tempMax = temp + 2,
                pressure = 1013,
                humidity = 50
            ),
            weather = listOf(
                WeatherConditionDto(
                    id = 500,
                    main = condition,
                    description = condition.lowercase(),
                    icon = "10d"
                )
            ),
            wind = WindDto(speed = 3.0, deg = 180),
            visibility = 10000,
            pop = 0.2,
            dtTxt = "2024-01-01 00:00:00"
        )
    }
}
