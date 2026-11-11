package com.weatherwise.app.ui.search

import com.weatherwise.app.domain.model.SearchResult
import com.weatherwise.app.domain.repository.SearchRepository
import com.weatherwise.app.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val searchRepository: SearchRepository = mockk()
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(searchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchCity updates searchResults when search succeeds`() = runTest {
        val results = listOf(
            SearchResult(name = "Paris", country = "FR", state = "IDF", lat = 48.8566, lon = 2.3522)
        )

        coEvery { searchRepository.searchCity("Paris") } returns Result.Success(results)

        viewModel.searchCity("Paris")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.searchResults.size)
        assertEquals("Paris", state.searchResults.first().name)
    }

    @Test
    fun `onQueryChanged with short query clears results`() = runTest {
        viewModel.onQueryChanged("a")

        val state = viewModel.uiState.value
        assertEquals("a", state.query)
        assertTrue(state.searchResults.isEmpty())
    }
}
