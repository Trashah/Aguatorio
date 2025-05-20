package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomBar
import androidx.compose.material3.CardDefaults

// Colores personalizados
val PrimaryBlue = Color(0xFF0076BF)
val SecondaryBlue = Color(0xFF5BB3E6)
val LightBlue = Color(0x00FFFFFF)

data class RecommendationGroup(
    val title: String,
    val icon: ImageVector,
    val recommendations: List<String>
)

@Composable
fun ActivitiesScreen(navController: NavHostController) {
    val recommendationsList = listOf(
        RecommendationGroup(
            "Niños y adolescentes (5 a 17 años)",
            Icons.Default.ChildCare,
            listOf(
                "Duración: Al menos 60 minutos diarios de actividad física moderada a vigorosa",
                "Tipo: Principalmente aeróbica (correr, nadar, saltar)",
                "Frecuencia de ejercicios intensos: Al menos 3 veces por semana incluir actividades que fortalezcan músculos y huesos"
            )
        ),
        RecommendationGroup(
            "Adultos (18 a 64 años)",
            Icons.Default.DirectionsRun,
            listOf(
                "150-300 minutos de actividad moderada o 75-150 minutos de actividad intensa semanal",
                "Incluir ejercicios de fortalecimiento muscular al menos 2 veces por semana"
            )
        ),
        RecommendationGroup(
            "Adultos mayores (65 años en adelante)",
            Icons.Default.Elderly,
            listOf(
                "Misma recomendación que adultos",
                "Incluir ejercicios de equilibrio y coordinación al menos 3 veces por semana"
            )
        ),
        RecommendationGroup(
            "Personas con enfermedades crónicas o discapacidades",
            Icons.Default.Favorite,
            listOf(
                "Adaptar las recomendaciones según condición física",
                "Incluir aeróbicos y fortalecimiento muscular en lo posible"
            )
        )
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryBlue,
            secondary = SecondaryBlue,
            background = LightBlue,
            surface = Color.White
        )
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    navController = navController,
                    currentDestination = navController.currentDestination
                )
            }
        ) { paddingValues ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // Importante para evitar que el contenido quede debajo de la barra
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { HeaderSection() }

                    items(recommendationsList) { group ->
                        RecommendationCard(group)
                    }

                    item {ActivityRecommendationTable()}
                }
            }
        }
    }
}


@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Actividad física recomendada",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue
        )
    }


}



@Composable
fun RecommendationCard(group: RecommendationGroup) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = group.icon,
                    contentDescription = group.title,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = group.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            group.recommendations.forEach { rec ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SecondaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = rec, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ActivityRecommendationTable() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
        color = Color(0xFFE1F1FA),
        shape = RoundedCornerShape(12.dp), // ✅ Usa shape aquí
        shadowElevation = 4.dp
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tabla resumen de actividad física recomendada semanalmente",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Encabezado
            TableRow(
                listOf(
                    "Grupo de Edad / Condición",
                    "Actividad Aeróbica",
                    "Intensidad",
                    "Frecuencia",
                    "Ejercicios Adicionales"
                ),
                isHeader = true
            )

            // Filas
            TableRow(listOf("Niños y adolescentes (5-17 años)", "60 min diarios", "Moderada a alta", "Todos los días", "3 días/semana: musculares y óseas"))
            TableRow(listOf("Adultos (18-64 años)", "150-300 min/semana", "Moderada", "5 días/semana aprox.", "2 días/semana: fuerza muscular"))
            TableRow(listOf("", "75-150 min/semana", "Intensa", "3 días/semana aprox.", ""))
            TableRow(listOf("Adultos mayores (65+ años)", "150-300 min/semana", "Moderada o mixta", "Repartida en la semana", "3 días: equilibrio + 2 días: fuerza muscular"))
            TableRow(listOf("Personas con enfermedades o discapacidades", "Adaptado a sus capacidades (ideal: 150 min/sem)", "Moderada", "Según condición", "En lo posible: fuerza muscular + equilibrio"))
        }
    }
}


@Composable
fun TableRow(data: List<String>, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        data.forEach { cell ->
            Text(
                text = cell,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                style = if (isHeader) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodySmall,
                fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
