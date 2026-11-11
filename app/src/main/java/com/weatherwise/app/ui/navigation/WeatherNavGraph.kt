package com.weatherwise.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.weatherwise.app.ui.forecast.ForecastScreen
import com.weatherwise.app.ui.forecast.ForecastViewModel
import com.weatherwise.app.ui.home.HomeScreen
import com.weatherwise.app.ui.home.HomeViewModel
import com.weatherwise.app.ui.search.SearchScreen
import com.weatherwise.app.ui.search.SearchViewModel

@Composable
fun WeatherNavGraph(
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    forecastViewModel: ForecastViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val homeUiState by homeViewModel.uiState.collectAsState()

    val screens = listOf(
        Screen.Home,
        Screen.Forecast,
        Screen.Search
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onOpenSettings = onOpenSettings
                )
            }
            composable(Screen.Forecast.route) {
                ForecastScreen(
                    viewModel = forecastViewModel,
                    lat = homeUiState.lat,
                    lon = homeUiState.lon
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = searchViewModel,
                    onCitySelected = { lat, lon, _ ->
                        homeViewModel.fetchWeatherForCoordinates(lat, lon)
                        forecastViewModel.fetchForecast(lat, lon)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
