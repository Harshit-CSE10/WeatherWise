package com.weatherwise.app.ui.components

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp

data class TempTrendPoint(
    val dayLabel: String,
    val tempMax: Double,
    val tempMin: Double
)

@Composable
fun TemperatureTrendChart(
    points: List<TempTrendPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val labelTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    val textPaint = remember(labelTextColor) {
        Paint().apply {
            color = labelTextColor
            textSize = 32f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
    }

    val secondaryTextPaint = remember(secondaryTextColor) {
        Paint().apply {
            color = secondaryTextColor
            textSize = 28f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val width = size.width
        val height = size.height
        val pointCount = points.size

        if (pointCount < 2) return@Canvas

        val maxTempOverall = points.maxOf { it.tempMax }
        val minTempOverall = points.minOf { it.tempMin }
        val tempRange = (maxTempOverall - minTempOverall).coerceAtLeast(1.0)

        val topPadding = 45f
        val bottomPadding = 45f
        val chartHeight = height - topPadding - bottomPadding

        val stepX = width / (pointCount - 1)

        val maxPositions = mutableListOf<Offset>()
        val minPositions = mutableListOf<Offset>()

        for (i in 0 until pointCount) {
            val x = i * stepX
            val pt = points[i]

            val yMax = topPadding + ((maxTempOverall - pt.tempMax) / tempRange * chartHeight).toFloat()
            val yMin = topPadding + ((maxTempOverall - pt.tempMin) / tempRange * chartHeight).toFloat()

            maxPositions.add(Offset(x, yMax))
            minPositions.add(Offset(x, yMin))
        }

        // Draw max temperature line (smooth Bezier curve)
        val maxPath = Path().apply {
            moveTo(maxPositions[0].x, maxPositions[0].y)
            for (i in 0 until maxPositions.size - 1) {
                val p1 = maxPositions[i]
                val p2 = maxPositions[i + 1]
                val control1 = Offset(p1.x + stepX / 2f, p1.y)
                val control2 = Offset(p2.x - stepX / 2f, p2.y)
                cubicTo(control1.x, control1.y, control2.x, control2.y, p2.x, p2.y)
            }
        }

        // Draw min temperature line (smooth Bezier curve)
        val minPath = Path().apply {
            moveTo(minPositions[0].x, minPositions[0].y)
            for (i in 0 until minPositions.size - 1) {
                val p1 = minPositions[i]
                val p2 = minPositions[i + 1]
                val control1 = Offset(p1.x + stepX / 2f, p1.y)
                val control2 = Offset(p2.x - stepX / 2f, p2.y)
                cubicTo(control1.x, control1.y, control2.x, control2.y, p2.x, p2.y)
            }
        }

        drawPath(
            path = maxPath,
            color = Color(0xFFFF7043),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        drawPath(
            path = minPath,
            color = Color(0xFF42A5F5),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        // Draw data points and text labels
        for (i in 0 until pointCount) {
            val maxPos = maxPositions[i]
            val minPos = minPositions[i]
            val pt = points[i]

            // Max point circle
            drawCircle(
                color = Color(0xFFFF7043),
                radius = 8f,
                center = maxPos
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = maxPos
            )

            // Min point circle
            drawCircle(
                color = Color(0xFF42A5F5),
                radius = 8f,
                center = minPos
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = minPos
            )

            // Text labels via native canvas
            drawIntoCanvas { canvas ->
                val maxLabel = "${pt.tempMax.toInt()}°"
                val minLabel = "${pt.tempMin.toInt()}°"

                canvas.nativeCanvas.drawText(
                    maxLabel,
                    maxPos.x,
                    maxPos.y - 14f,
                    textPaint
                )

                canvas.nativeCanvas.drawText(
                    minLabel,
                    minPos.x,
                    minPos.y + 32f,
                    secondaryTextPaint
                )
            }
        }
    }
}
