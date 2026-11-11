package com.weatherwise.app.ui.components

import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import kotlin.random.Random

@Composable
fun WeatherCanvasAnimation(
    condition: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isReducedMotionEnabled = remember(context) {
        try {
            val durationScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
            val transitionScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.TRANSITION_ANIMATION_SCALE,
                1f
            )
            durationScale == 0f || transitionScale == 0f
        } catch (_: Exception) {
            false
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "WeatherAnimation")

    val progress by if (!isReducedMotionEnabled) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 6000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "WeatherAnimProgress"
        )
    } else {
        remember { mutableFloatStateOf(0.5f) }
    }

    val lowerCondition = condition.lowercase()

    Canvas(modifier = modifier.fillMaxSize()) {
        when {
            lowerCondition.contains("clear") || lowerCondition.contains("sun") -> {
                drawSunnyAnimation(progress, isReducedMotionEnabled)
            }
            lowerCondition.contains("rain") || lowerCondition.contains("drizzle") || lowerCondition.contains("thunderstorm") -> {
                drawRainAnimation(progress, isReducedMotionEnabled)
            }
            lowerCondition.contains("snow") -> {
                drawSnowAnimation(progress, isReducedMotionEnabled)
            }
            else -> {
                drawCloudyAnimation(progress, isReducedMotionEnabled)
            }
        }
    }
}

private fun DrawScope.drawSunnyAnimation(progress: Float, isReducedMotion: Boolean) {
    val center = Offset(size.width * 0.8f, size.height * 0.25f)
    val sunRadius = size.width * 0.12f

    // Glow aura
    drawCircle(
        color = Color(0x33FFBB00),
        radius = sunRadius * 1.5f,
        center = center
    )

    // Sun core
    drawCircle(
        color = Color(0xFFFFCC00),
        radius = sunRadius,
        center = center
    )

    // Sun rays
    val rayCount = 8
    val rayLength = sunRadius * 0.6f
    val rotation = if (isReducedMotion) 0f else progress * 360f

    rotate(degrees = rotation, pivot = center) {
        for (i in 0 until rayCount) {
            val angle = Math.toRadians((i * (360.0 / rayCount)))
            val startX = center.x + (sunRadius + 10f) * kotlin.math.cos(angle).toFloat()
            val startY = center.y + (sunRadius + 10f) * kotlin.math.sin(angle).toFloat()
            val endX = center.x + (sunRadius + 10f + rayLength) * kotlin.math.cos(angle).toFloat()
            val endY = center.y + (sunRadius + 10f + rayLength) * kotlin.math.sin(angle).toFloat()

            drawLine(
                color = Color(0xFFFFD700),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 6f
            )
        }
    }
}

private fun DrawScope.drawRainAnimation(progress: Float, isReducedMotion: Boolean) {
    val dropCount = 30
    val random = Random(42)

    for (i in 0 until dropCount) {
        val xRatio = (random.nextFloat() + i * 0.033f) % 1f
        val startYRatio = random.nextFloat()
        val yOffset = if (isReducedMotion) 0f else (progress + startYRatio) % 1f

        val startX = size.width * xRatio
        val startY = size.height * yOffset
        val dropLength = 35f

        drawLine(
            color = Color(0x8080D0FF),
            start = Offset(startX, startY),
            end = Offset(startX - 5f, startY + dropLength),
            strokeWidth = 3f
        )
    }
}

private fun DrawScope.drawSnowAnimation(progress: Float, isReducedMotion: Boolean) {
    val flakeCount = 35
    val random = Random(123)

    for (i in 0 until flakeCount) {
        val xRatio = (random.nextFloat() + i * 0.03f) % 1f
        val startYRatio = random.nextFloat()
        val yOffset = if (isReducedMotion) 0.5f else (progress + startYRatio) % 1f
        val wobble = if (isReducedMotion) 0f else kotlin.math.sin((progress * 2 * Math.PI + i).toDouble()).toFloat() * 15f

        val x = size.width * xRatio + wobble
        val y = size.height * yOffset
        val flakeRadius = 4f + (i % 3) * 2f

        drawCircle(
            color = Color(0xCCFFFFFF),
            radius = flakeRadius,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawCloudyAnimation(progress: Float, isReducedMotion: Boolean) {
    val cloudOffset = if (isReducedMotion) 0f else kotlin.math.sin(progress * Math.PI * 2).toFloat() * 25f

    val cloudPath = Path().apply {
        val startX = size.width * 0.1f + cloudOffset
        val startY = size.height * 0.25f

        moveTo(startX, startY)
        cubicTo(
            startX + 40f, startY - 50f,
            startX + 120f, startY - 50f,
            startX + 160f, startY
        )
        cubicTo(
            startX + 200f, startY - 20f,
            startX + 240f, startY + 40f,
            startX + 200f, startY + 60f
        )
        lineTo(startX - 20f, startY + 60f)
        cubicTo(
            startX - 60f, startY + 40f,
            startX - 40f, startY - 20f,
            startX, startY
        )
        close()
    }

    drawPath(
        path = cloudPath,
        color = Color(0x40FFFFFF)
    )
}
