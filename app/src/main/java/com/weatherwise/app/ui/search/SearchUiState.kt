package com.weatherwise.app.ui.search

import com.weatherwise.app.domain.model.SearchResult

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<SearchResult> = emptyList(),
    val error: String? = null
)
