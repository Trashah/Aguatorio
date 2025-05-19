package com.example.myapplication.ui.components

import com.example.myapplication.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WaterGlassScreen() {
    var progress by remember { mutableStateOf(0f) }
    var completedGlasses by remember { mutableStateOf(0) }
    val maxFills = 4 // Cuatro veces = 100% del vaso
    val fillIncrement = 1f / maxFills // 25% por llenado
    val showJar = progress >= 1f // Mostrar la jarra cuando el vaso esté lleno

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        WaterGlass(progress, Modifier.size(150.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (progress >= 1f) {
                progress = 0f
                completedGlasses++ // Incrementar el contador de vasos completados
            } else {
                progress += fillIncrement
            }
        }) {
            Text(text = "Llenar vaso")
        }

        if (showJar) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // Primer vaso completado
                WaterGlass(
                    progress = 1f,
                    modifier = Modifier.size(80.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Segundo vaso completado con contador
                Box {
                    WaterGlass(
                        progress = 1f,
                        modifier = Modifier.size(80.dp)
                    )
                    
                    // Mostrar el contador solo si hay 3 o más vasos
                    if (completedGlasses >= 3) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                                .offset(x = 8.dp, y = (-4).dp)
                        ) {
                            Text(
                                text = "+${completedGlasses - 2}",
                                color = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaterGlass(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val waterColor = Color(0xFF88D8FF) // Color azul claro transparente
    val glassColor = Color.Black // Color negro para el contorno
    val glassShineColor = Color(0xAAE1F5FE) // Color del brillo del vidrio
    
    // Animación para el agua
    val infiniteTransition = rememberInfiniteTransition(label = "water")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    // Animación para las burbujas
    val bubblePhases = remember { List(15) { Random.nextFloat() * 2f * Math.PI.toFloat() } }
    val bubbleSpeeds = remember { List(15) { Random.nextFloat() * 0.5f + 0.5f } }
    val bubblePositions = remember { List(15) { Random.nextFloat() } }
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Dibujar el vaso
            drawGlass(glassColor)
            
            // Dibujar el agua y las burbujas si hay progreso
            if (progress > 0f) {
                drawWater(waterColor, animatedProgress)
                drawBubbles(waterColor.copy(alpha = 0.6f), animatedProgress, bubblePhases, bubbleSpeeds, bubblePositions, wavePhase)
            }
            
            // Dibujar el brillo del vaso
            drawGlassShine(glassShineColor)
        }
    }
}

private fun DrawScope.drawGlass(color: Color) {
    val width = size.width
    val height = size.height
    val strokeWidth = 4f
    
    // Definir las proporciones del vaso
    val baseWidth = width * 0.65f // Base ligeramente más ancha
    val topWidth = width * 0.7f   // Parte superior un poco más ancha que la base
    val glassHeight = height * 0.95f
    
    // Crear el path del vaso
    val glassPath = Path().apply {
        // Comenzar desde la base izquierda
        moveTo((width - baseWidth) / 2, glassHeight)
        
        // Base del vaso
        lineTo((width + baseWidth) / 2, glassHeight)
        
        // Lado derecho del vaso (recto con ligera inclinación)
        lineTo((width + topWidth) / 2, height * 0.05f)
        
        // Borde superior
        lineTo((width - topWidth) / 2, height * 0.05f)
        
        // Cerrar el path volviendo a la base
        close()
    }
    
    // Dibujar el contorno del vaso
    drawPath(
        path = glassPath,
        color = color,
        style = Stroke(width = strokeWidth)
    )
}

private fun DrawScope.drawWater(color: Color, progress: Float) {
    val width = size.width
    val height = size.height
    val baseWidth = width * 0.65f
    val maxWaterHeight = height * 0.95f
    val waterHeight = maxWaterHeight * progress
    val startY = height * 0.95f
    val startX = (width - baseWidth) / 2
    
    // Solo dibujamos agua si hay progreso
    if (progress > 0f) {
        val waterPath = Path().apply {
            moveTo(startX, startY)
            lineTo(startX, startY - waterHeight)
            lineTo(startX + baseWidth, startY - waterHeight)
            lineTo(startX + baseWidth, startY)
            close()
        }

        // Gradiente principal del agua
        drawPath(
            path = waterPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.7f),
                    color.copy(alpha = 0.9f)
                ),
                startY = startY - waterHeight,
                endY = startY
            )
        )

        // Efecto de brillo en la superficie
        drawPath(
            path = Path().apply {
                val surfaceY = startY - waterHeight
                moveTo(startX, surfaceY)
                lineTo(startX + baseWidth, surfaceY)
                lineTo(startX + baseWidth * 0.8f, surfaceY + 10f)
                lineTo(startX + baseWidth * 0.2f, surfaceY + 10f)
                close()
            },
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.3f),
                    Color.White.copy(alpha = 0.1f),
                    Color.White.copy(alpha = 0.0f)
                )
            )
        )

        // Efecto de reflejo lateral
        drawPath(
            path = Path().apply {
                moveTo(startX, startY)
                lineTo(startX, startY - waterHeight)
                lineTo(startX + baseWidth * 0.2f, startY - waterHeight)
                lineTo(startX + baseWidth * 0.1f, startY)
                close()
            },
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.2f),
                    Color.Transparent
                )
            )
        )

        // Pequeñas burbujas decorativas
        repeat(5) { index ->
            val bubbleX = startX + baseWidth * (0.2f + index * 0.15f)
            val bubbleY = startY - waterHeight * (0.3f + index * 0.1f)
            drawCircle(
                color = Color.White.copy(alpha = 0.15f),
                radius = 3.dp.toPx(),
                center = Offset(bubbleX, bubbleY)
            )
        }
    }
}

private fun DrawScope.drawBubbles(
    color: Color,
    progress: Float,
    phases: List<Float>,
    speeds: List<Float>,
    horizontalPositions: List<Float>,
    globalPhase: Float
) {
    val width = size.width
    val height = size.height
    val baseWidth = width * 0.65f
    val maxWaterHeight = height * 0.9f * progress
    
    phases.forEachIndexed { index, phase ->
        val x = width * 0.5f + (horizontalPositions[index] - 0.5f) * baseWidth * 0.8f
        val baseY = height - maxWaterHeight
        val bubbleProgress = ((globalPhase * speeds[index] + phase) % (2f * Math.PI.toFloat())) / (2f * Math.PI.toFloat())
        val y = baseY + maxWaterHeight * bubbleProgress
        
        // Solo dibujar burbujas en la parte inferior del agua
        if (y >= baseY && y <= height - maxWaterHeight * 0.3f) {
            drawCircle(
                color = color,
                radius = 1.5f + (1 - bubbleProgress) * 1.5f,
                center = Offset(x, y)
            )
        }
    }
}

private fun DrawScope.drawGlassShine(color: Color) {
    val width = size.width
    val height = size.height
    
    // Brillo superior izquierdo más sutil
    drawLine(
        color = color.copy(alpha = 0.4f),
        start = Offset(width * 0.3f, height * 0.1f),
        end = Offset(width * 0.35f, height * 0.15f),
        strokeWidth = 2f
    )
}

