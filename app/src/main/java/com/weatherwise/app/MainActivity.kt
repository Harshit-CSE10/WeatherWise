package com.weatherwise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.weatherwise.app.ui.MainViewModel
import com.weatherwise.app.ui.navigation.WeatherNavGraph
import com.weatherwise.app.ui.settings.SettingsDialog
import com.weatherwise.app.ui.theme.WeatherWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by mainViewModel.themeMode.collectAsState()
            var showSettingsDialog by remember { mutableStateOf(false) }

            WeatherWiseTheme(themeMode = themeMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherNavGraph(
                        onOpenSettings = { showSettingsDialog = true }
                    )

                    if (showSettingsDialog) {
                        SettingsDialog(
                            currentThemeMode = themeMode,
                            onThemeSelected = { newMode ->
                                mainViewModel.setThemeMode(newMode)
                            },
                            onDismiss = { showSettingsDialog = false }
                        )
                    }
                }
            }
        }
    }
}
