package com.weatherwise.app.data.mapper

import com.weatherwise.app.data.local.db.entity.CurrentWeatherEntity
import com.weatherwise.app.data.remote.dto.CurrentWeatherDto
import com.weatherwise.app.domain.model.CurrentWeather

object WeatherMapper {
    fun dtoToDomain(dto: CurrentWeatherDto): CurrentWeather {
        val condition = dto.weather.firstOrNull()
        return CurrentWeather(
            cityName = dto.name,
            country = dto.sys.country ?: "",
            temperature = dto.main.temp,
            feelsLike = dto.main.feelsLike,
            tempMin = dto.main.tempMin,
            tempMax = dto.main.tempMax,
            humidity = dto.main.humidity,
            pressure = dto.main.pressure,
            windSpeed = dto.wind.speed,
            windDeg = dto.wind.deg,
            visibility = dto.visibility,
            cloudiness = dto.clouds.all,
            condition = condition?.main ?: "Unknown",
            conditionDescription = condition?.description ?: "",
            conditionIcon = condition?.icon ?: "01d",
            sunrise = dto.sys.sunrise,
            sunset = dto.sys.sunset,
            dt = dto.dt,
            timezone = dto.timezone,
            lat = dto.coord.lat,
            lon = dto.coord.lon
        )
    }

    fun entityToDomain(entity: CurrentWeatherEntity): CurrentWeather {
        return CurrentWeather(
            cityName = entity.cityName,
            country = entity.country,
            temperature = entity.temperature,
            feelsLike = entity.feelsLike,
            tempMin = entity.tempMin,
            tempMax = entity.tempMax,
            humidity = entity.humidity,
            pressure = entity.pressure,
            windSpeed = entity.windSpeed,
            windDeg = entity.windDeg,
            visibility = entity.visibility,
            cloudiness = entity.cloudiness,
            condition = entity.condition,
            conditionDescription = entity.conditionDescription,
            conditionIcon = entity.conditionIcon,
            sunrise = entity.sunrise,
            sunset = entity.sunset,
            dt = entity.dt,
            timezone = entity.timezone,
            lat = entity.lat,
            lon = entity.lon
        )
    }

    fun domainToEntity(domain: CurrentWeather): CurrentWeatherEntity {
        return CurrentWeatherEntity(
            cityName = domain.cityName,
            country = domain.country,
            temperature = domain.temperature,
            feelsLike = domain.feelsLike,
            tempMin = domain.tempMin,
            tempMax = domain.tempMax,
            humidity = domain.humidity,
            pressure = domain.pressure,
            windSpeed = domain.windSpeed,
            windDeg = domain.windDeg,
            visibility = domain.visibility,
            cloudiness = domain.cloudiness,
            condition = domain.condition,
            conditionDescription = domain.conditionDescription,
            conditionIcon = domain.conditionIcon,
            sunrise = domain.sunrise,
            sunset = domain.sunset,
            dt = domain.dt,
            timezone = domain.timezone,
            lat = domain.lat,
            lon = domain.lon
        )
    }
}
