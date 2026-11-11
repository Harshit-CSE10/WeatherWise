package com.weatherwise.app.data.mapper

import com.weatherwise.app.data.local.db.entity.CurrentWeatherEntity
import com.weatherwise.app.data.remote.dto.*
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun `dtoToDomain maps CurrentWeatherDto to CurrentWeather domain model correctly`() {
        val dto = CurrentWeatherDto(
            coord = CoordDto(lon = 13.405, lat = 52.52),
            weather = listOf(
                WeatherConditionDto(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            main = MainDto(
                temp = 22.5,
                feelsLike = 21.8,
                tempMin = 20.0,
                tempMax = 25.0,
                pressure = 1013,
                humidity = 45
            ),
            visibility = 10000,
            wind = WindDto(speed = 3.6, deg = 180),
            clouds = CloudsDto(all = 0),
            dt = 1700000000L,
            sys = SysDto(country = "DE", sunrise = 1699960000L, sunset = 1700000000L),
            timezone = 3600,
            id = 2950159,
            name = "Berlin"
        )

        val domain = WeatherMapper.dtoToDomain(dto)

        assertEquals("Berlin", domain.cityName)
        assertEquals("DE", domain.country)
        assertEquals(22.5, domain.temperature, 0.01)
        assertEquals(21.8, domain.feelsLike, 0.01)
        assertEquals(20.0, domain.tempMin, 0.01)
        assertEquals(25.0, domain.tempMax, 0.01)
        assertEquals(45, domain.humidity)
        assertEquals(1013, domain.pressure)
        assertEquals(3.6, domain.windSpeed, 0.01)
        assertEquals("Clear", domain.condition)
        assertEquals("clear sky", domain.conditionDescription)
        assertEquals("01d", domain.conditionIcon)
    }

    @Test
    fun `entityToDomain maps CurrentWeatherEntity to domain correctly`() {
        val entity = CurrentWeatherEntity(
            cityName = "Tokyo",
            country = "JP",
            temperature = 18.0,
            feelsLike = 17.5,
            tempMin = 15.0,
            tempMax = 20.0,
            humidity = 60,
            pressure = 1015,
            windSpeed = 4.0,
            windDeg = 90,
            visibility = 10000,
            cloudiness = 20,
            condition = "Clouds",
            conditionDescription = "few clouds",
            conditionIcon = "02d",
            sunrise = 1700000000L,
            sunset = 1700040000L,
            dt = 1700020000L,
            timezone = 32400,
            lat = 35.6762,
            lon = 139.6503
        )

        val domain = WeatherMapper.entityToDomain(entity)

        assertEquals("Tokyo", domain.cityName)
        assertEquals("JP", domain.country)
        assertEquals(18.0, domain.temperature, 0.01)
        assertEquals("Clouds", domain.condition)
    }

    @Test
    fun `domainToEntity maps CurrentWeather domain model to Entity correctly`() {
        val entity = CurrentWeatherEntity(
            cityName = "London",
            country = "GB",
            temperature = 12.0,
            feelsLike = 10.5,
            tempMin = 8.0,
            tempMax = 14.0,
            humidity = 80,
            pressure = 1008,
            windSpeed = 6.2,
            windDeg = 240,
            visibility = 8000,
            cloudiness = 90,
            condition = "Rain",
            conditionDescription = "light rain",
            conditionIcon = "10d",
            sunrise = 1700000000L,
            sunset = 1700030000L,
            dt = 1700010000L,
            timezone = 0,
            lat = 51.5074,
            lon = -0.1278
        )

        val domain = WeatherMapper.entityToDomain(entity)
        val convertedEntity = WeatherMapper.domainToEntity(domain)

        assertEquals(entity.cityName, convertedEntity.cityName)
        assertEquals(entity.country, convertedEntity.country)
        assertEquals(entity.temperature, convertedEntity.temperature, 0.01)
        assertEquals(entity.condition, convertedEntity.condition)
    }
}
