package com.weatherwise.app.data.remote.api

import com.weatherwise.app.data.remote.dto.GeocodingDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): List<GeocodingDto>
}
