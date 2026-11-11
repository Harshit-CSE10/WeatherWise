package com.weatherwise.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherwise.app.domain.repository.SearchRepository
import com.weatherwise.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        if (query.length < 2) {
            _uiState.update { it.copy(searchResults = emptyList(), error = null, isLoading = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(500) // Debounce user typing
            searchCity(query)
        }
    }

    fun searchCity(query: String = _uiState.value.query) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = searchRepository.searchCity(query)) {
                is Result.Loading -> {}
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchResults = result.data,
                            error = if (result.data.isEmpty()) "No matching cities found" else null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    fun clearQuery() {
        _uiState.update { SearchUiState() }
    }
}
