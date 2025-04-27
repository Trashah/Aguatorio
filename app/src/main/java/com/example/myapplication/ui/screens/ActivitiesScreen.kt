package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomBar

data class Activity(
    val name: String,
    val icon: ImageVector,
    val duration: String,
    val waterRecommendation: String,
    val intensity: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(
    navController: NavHostController
) {
    val activities = remember {
        listOf(
            Activity(
                "Correr",
                Icons.Default.DirectionsRun,
                "30-60 min",
                "500-1000 ml",
                "Alta"
            ),
            Activity(
                "Ciclismo",
                Icons.Default.DirectionsBike,
                "1-2 horas",
                "750-1500 ml",
                "Media-Alta"
            ),
            Activity(
                "Natación",
                Icons.Default.Pool,
                "45-90 min",
                "600-1200 ml",
                "Alta"
            ),
            Activity(
                "Yoga",
                Icons.Default.SelfImprovement,
                "30-60 min",
                "300-500 ml",
                "Baja"
            ),
            Activity(
                "Gimnasio",
                Icons.Default.FitnessCenter,
                "60-90 min",
                "700-1000 ml",
                "Media-Alta"
            ),
            Activity(
                "Caminata",
                Icons.Default.DirectionsWalk,
                "30-60 min",
                "400-800 ml",
                "Baja-Media"
            )
        )
    }

    var selectedActivity by remember { mutableStateOf<Activity?>(null) }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentDestination = navController.currentDestination
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Implementar registro de nueva actividad */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar actividad"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Actividades",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activities) { activity ->
                    ActivityCard(
                        activity = activity,
                        isSelected = activity == selectedActivity,
                        onClick = {
                            selectedActivity = if (selectedActivity == activity) null else activity
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivityCard(
    activity: Activity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = activity.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = activity.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Intensidad: ${activity.intensity}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoColumn(
                        title = "Duración",
                        value = activity.duration
                    )
                    InfoColumn(
                        title = "Agua recomendada",
                        value = activity.waterRecommendation
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoColumn(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
} 