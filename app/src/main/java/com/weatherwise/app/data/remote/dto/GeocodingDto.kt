package com.weatherwise.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeocodingDto(
    @Json(name = "name") val name: String,
    @Json(name = "local_names") val localNames: Map<String, String>? = null,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "country") val country: String,
    @Json(name = "state") val state: String? = null
)
