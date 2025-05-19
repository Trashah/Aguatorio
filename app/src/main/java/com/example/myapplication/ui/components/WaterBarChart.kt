package com.example.myapplication.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WaterBarChart(
    data: List<Float>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    maxValue: Float = data.maxOrNull() ?: 0f
) {
    val barColor = MaterialTheme.colorScheme.primary
    val animatedData = data.map { value ->
        var animatedValue by remember { mutableStateOf(0f) }
        LaunchedEffect(value) {
            animate(
                initialValue = animatedValue,
                targetValue = value,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            ) { currentValue, _ ->
                animatedValue = currentValue
            }
        }
        animatedValue
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gráfico de barras
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
        ) {
            val barWidth = (size.width - (data.size + 1) * 8f) / data.size
            val heightRatio = size.height / maxValue

            // Dibujar las barras
            animatedData.forEachIndexed { index, value ->
                val x = 8f + index * (barWidth + 8f)
                val height = value * heightRatio
                val y = size.height - height

                // Barra principal
                drawRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, height)
                )

                // Reflejo (efecto de agua)
                drawRect(
                    color = barColor.copy(alpha = 0.3f),
                    topLeft = Offset(x, y - 4f),
                    size = Size(barWidth, 4f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Etiquetas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
} 