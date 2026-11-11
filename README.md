# WeatherWise - Native Android Weather Application

**WeatherWise** is a modern native Android weather application built with Kotlin, Jetpack Compose, MVVM architecture, StateFlow, Coroutines, Retrofit/OkHttp, Room Database, DataStore, and Navigation Compose.

---

## Features

1. **Current Weather (Home Screen):**
   - Displays city name, country, temperature, feels-like temperature, weather condition, humidity, wind speed, pressure, visibility, and sunrise/sunset times.
   - Dynamic Canvas weather background animations for Clear/Sunny, Rainy, Snowy, and Cloudy conditions (respecting reduced-motion settings).
   - Outdated data banner when displaying cached data offline.

2. **City Search (Geocoding API):**
   - Instant search with debounced user input powered by OpenWeatherMap Geocoding API (`/geo/1.0/direct`).
   - Multiple matching city results displaying name, state, and country.
   - Selecting a city updates coordinates and refreshes weather data seamlessly.

3. **5-Day Forecast & Custom Canvas Chart:**
   - 5-day daily high/low temperature forecast cards with condition icons.
   - Custom Canvas-based Temperature Trend Chart rendering smooth Bezier curves for high and low temperature curves (no third-party chart libraries).
   - 3-hour step forecast list, honestly labeled as **3-Hour Step Forecast**.

4. **Location Integration & Permissions:**
   - GPS Location retrieval using Google Play Services `FusedLocationProviderClient`.
   - Runtime location permission requests with graceful fallback to standard defaults or searched cities if denied.

5. **Offline Cache & Room Database:**
   - Room Database (`WeatherDatabase`) caches the latest successful weather and forecast payloads.
   - Offline fallback guarantees uninterrupted UI display with visible cache staleness indicators.

6. **Appearance & Theme Mode:**
   - Light Theme, Dark Theme, and System Default theme support.
   - Preferences persisted via Jetpack DataStore (`SettingsDataStore`).

7. **Error & Setup Resilience:**
   - Clear setup card and guidance if the OpenWeatherMap API key is missing.
   - Comprehensive error, loading, empty, and retry states on all screens.

---

## Architecture

The project adheres to clean architecture and MVVM design patterns:

```
com.weatherwise.app/
├── WeatherWiseApplication.kt       # Application class annotated with @HiltAndroidApp
├── MainActivity.kt                 # Entry point hosting compose Navigation Graph & Theme
├── data/                           # Data layer
│   ├── local/
│   │   ├── datastore/              # DataStore for Theme preferences
│   │   └── db/                     # Room DB, Dao, and Entities for offline caching
│   ├── mapper/                     # DTO <-> Domain <-> Entity mappers & aggregation
│   ├── remote/                     # Retrofit services, Moshi DTOs, ApiKeyInterceptor
│   └── repository/                 # Repository implementations (Weather, Search, Settings)
├── di/                             # Hilt DI Modules (Network, Database, Repository)
├── domain/                         # Domain layer
│   ├── model/                      # Data models (CurrentWeather, DailyForecast, HourlyForecast, etc.)
│   └── repository/                 # Repository interfaces
├── ui/                             # Presentation layer
│   ├── theme/                      # Material 3 Color, Type, and Theme
│   ├── components/                 # Canvas animations, Canvas Trend Chart, Error/Loading states
│   ├── home/                       # HomeScreen & HomeViewModel
│   ├── search/                     # SearchScreen & SearchViewModel
│   ├── forecast/                   # ForecastScreen & ForecastViewModel
│   ├── settings/                   # SettingsDialog for theme selection
│   └── navigation/                 # WeatherNavGraph & Screen routes
└── util/                           # Constants, DateTimeUtils, LocationHelper, Result wrapper
```

---

## Setup & API Key Configuration

1. **Obtain an OpenWeatherMap API Key:**
   - Sign up for a free tier API key at [OpenWeatherMap](https://openweathermap.org/api).

2. **Configure `local.properties`:**
   - Create or update the `local.properties` file in the root directory of the project:
     ```properties
     sdk.dir=/path/to/your/Android/sdk
     OPENWEATHER_API_KEY=your_actual_api_key_here
     ```
   - Reference example configuration in `local.properties.example`.

3. **Build & Run:**
   - Build the debug APK and run unit tests:
     ```bash
     ./gradlew assembleDebug testDebugUnitTest
     ```
