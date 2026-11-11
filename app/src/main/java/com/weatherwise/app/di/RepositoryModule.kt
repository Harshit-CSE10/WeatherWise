package com.weatherwise.app.di

import com.weatherwise.app.data.repository.SearchRepositoryImpl
import com.weatherwise.app.data.repository.SettingsRepositoryImpl
import com.weatherwise.app.data.repository.WeatherRepositoryImpl
import com.weatherwise.app.domain.repository.SearchRepository
import com.weatherwise.app.domain.repository.SettingsRepository
import com.weatherwise.app.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
