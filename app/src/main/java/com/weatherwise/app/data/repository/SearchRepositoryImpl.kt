package com.weatherwise.app.data.repository

import com.weatherwise.app.BuildConfig
import com.weatherwise.app.data.mapper.GeocodingMapper
import com.weatherwise.app.data.remote.api.GeocodingApiService
import com.weatherwise.app.domain.model.SearchResult
import com.weatherwise.app.domain.repository.SearchRepository
import com.weatherwise.app.util.Result
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApiService,
    @Named("api_key") private val apiKey: String = BuildConfig.OPENWEATHER_API_KEY
) : SearchRepository {
    override suspend fun searchCity(query: String): Result<List<SearchResult>> {
        if (query.isBlank()) {
            return Result.Success(emptyList())
        }
        if (apiKey.isBlank() || apiKey == "YOUR_KEY_HERE") {
            return Result.Error("API key is missing or not configured. Please add OPENWEATHER_API_KEY to local.properties.")
        }
        return try {
            val dtos = geocodingApi.searchCity(query)
            Result.Success(GeocodingMapper.dtoListToDomain(dtos))
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to search for city", e)
        }
    }
}
