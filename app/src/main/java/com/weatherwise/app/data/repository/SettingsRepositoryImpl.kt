package com.weatherwise.app.data.repository

import com.weatherwise.app.data.local.datastore.SettingsDataStore
import com.weatherwise.app.domain.model.ThemeMode
import com.weatherwise.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override val themeMode: Flow<ThemeMode> = settingsDataStore.themeMode

    override suspend fun setThemeMode(mode: ThemeMode) {
        settingsDataStore.setThemeMode(mode)
    }
}
