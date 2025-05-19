package com.example.myapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.myapplication.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBar {
        // Lista de items del menú inferior
        val items = listOf(
            Triple(Screen.Statistics, Icons.Default.BarChart, "Estadísticas"),
            Triple(Screen.Trophies, Icons.Default.EmojiEvents, "Trofeos"),
            Triple(Screen.Main, Icons.Default.WaterDrop, "Inicio"),
            Triple(Screen.Activities, Icons.Default.Loop, "Actividades"),
            Triple(Screen.Settings, Icons.Default.Settings, "Configuración")
        )

        items.forEach { (screen, icon, label) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
} 