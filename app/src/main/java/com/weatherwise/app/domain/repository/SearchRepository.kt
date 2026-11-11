package com.weatherwise.app.domain.repository

import com.weatherwise.app.domain.model.SearchResult
import com.weatherwise.app.util.Result

interface SearchRepository {
    suspend fun searchCity(query: String): Result<List<SearchResult>>
}
