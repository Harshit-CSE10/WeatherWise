package com.weatherwise.app.data.mapper

import com.weatherwise.app.data.remote.dto.ForecastDto
import com.weatherwise.app.data.remote.dto.ForecastItemDto
import com.weatherwise.app.domain.model.DailyForecast
import com.weatherwise.app.domain.model.ForecastData
import com.weatherwise.app.domain.model.HourlyForecast
import com.weatherwise.app.util.DateTimeUtils

object ForecastMapper {
    fun dtoToDomain(dto: ForecastDto): ForecastData {
        val hourlyForecasts = dto.list.map { item -> itemToHourly(item) }
        val dailyForecasts = groupByDay(hourlyForecasts, dto.city.timezone)
        return ForecastData(
            cityName = dto.city.name,
            hourlyForecasts = hourlyForecasts,
            dailyForecasts = dailyForecasts
        )
    }

    fun itemToHourly(item: ForecastItemDto): HourlyForecast {
        val condition = item.weather.firstOrNull()
        return HourlyForecast(
            dt = item.dt,
            temperature = item.main.temp,
            feelsLike = item.main.feelsLike,
            tempMin = item.main.tempMin,
            tempMax = item.main.tempMax,
            humidity = item.main.humidity,
            pressure = item.main.pressure,
            windSpeed = item.wind.speed,
            condition = condition?.main ?: "Unknown",
            conditionDescription = condition?.description ?: "",
            conditionIcon = condition?.icon ?: "01d",
            pop = item.pop
        )
    }

    fun groupByDay(
        hourlyForecasts: List<HourlyForecast>,
        timezoneOffset: Int = 0
    ): List<DailyForecast> {
        return hourlyForecasts
            .groupBy { DateTimeUtils.epochToLocalDate(it.dt, timezoneOffset) }
            .map { (date, forecasts) ->
                val mostCommon = forecasts.groupBy { it.condition }
                    .maxByOrNull { it.value.size }
                    ?.value?.firstOrNull()
                DailyForecast(
                    date = date,
                    tempMin = forecasts.minOf { it.tempMin },
                    tempMax = forecasts.maxOf { it.tempMax },
                    condition = mostCommon?.condition ?: "Unknown",
                    conditionIcon = mostCommon?.conditionIcon ?: "01d",
                    humidity = forecasts.map { it.humidity }.average().toInt(),
                    windSpeed = forecasts.map { it.windSpeed }.average(),
                    pop = forecasts.maxOf { it.pop },
                    hourlyForecasts = forecasts
                )
            }
            .sortedBy { it.date }
    }
}
