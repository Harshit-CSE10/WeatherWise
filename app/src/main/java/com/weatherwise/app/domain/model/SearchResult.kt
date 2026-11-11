package com.weatherwise.app.domain.model

data class SearchResult(
    val name: String,
    val country: String,
    val state: String?,
    val lat: Double,
    val lon: Double
) {
    val displayName: String
        get() = buildString {
            append(name)
            state?.let { append(", $it") }
            append(", $country")
        }
}
