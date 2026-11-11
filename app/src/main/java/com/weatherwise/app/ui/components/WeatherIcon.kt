package com.weatherwise.app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WeatherIcon(
    condition: String,
    iconCode: String = "",
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    tint: Color? = null
) {
    val (vector, defaultTint) = getWeatherIconAndColor(condition, iconCode)

    Icon(
        imageVector = vector,
        contentDescription = condition,
        tint = tint ?: defaultTint,
        modifier = modifier.size(size)
    )
}

fun getWeatherIconAndColor(condition: String, iconCode: String): Pair<ImageVector, Color> {
    val lower = condition.lowercase()
    val isNight = iconCode.endsWith("n")

    return when {
        lower.contains("sun") || lower.contains("clear") -> {
            if (isNight) {
                Icons.Default.NightsStay to Color(0xFF90CAF9)
            } else {
                Icons.Default.WbSunny to Color(0xFFFFB300)
            }
        }
        lower.contains("rain") || lower.contains("drizzle") -> {
            Icons.Default.Grain to Color(0xFF42A5F5)
        }
        lower.contains("thunder") || lower.contains("storm") -> {
            Icons.Default.FlashOn to Color(0xFFFFD54F)
        }
        lower.contains("snow") -> {
            Icons.Default.AcUnit to Color(0xFFE0F7FA)
        }
        lower.contains("cloud") -> {
            Icons.Default.Cloud to Color(0xFFB0BEC5)
        }
        lower.contains("mist") || lower.contains("fog") || lower.contains("haze") -> {
            Icons.Default.Dehaze to Color(0xFFCFD8DC)
        }
        else -> {
            Icons.Default.WbCloudy to Color(0xFF90A4AE)
        }
    }
}
