package com.weatherwise.app.data.mapper

import com.weatherwise.app.data.remote.dto.GeocodingDto
import com.weatherwise.app.domain.model.SearchResult

object GeocodingMapper {
    fun dtoToDomain(dto: GeocodingDto): SearchResult {
        return SearchResult(
            name = dto.name,
            country = dto.country,
            state = dto.state,
            lat = dto.lat,
            lon = dto.lon
        )
    }

    fun dtoListToDomain(dtos: List<GeocodingDto>): List<SearchResult> {
        return dtos.map { dtoToDomain(it) }
    }
}
