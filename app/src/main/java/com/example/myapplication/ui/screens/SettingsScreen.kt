package com.example.myapplication.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomBar
import androidx.work.WorkManager
import com.example.myapplication.utils.NotificationUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    var darkModeEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Español") }
    var metaAgua by remember { mutableStateOf("2100") }
    var unidadMedida by remember { mutableStateOf("ml") }

    // Obtener el estado actual de las notificaciones desde SharedPreferences
    val sharedPrefs = remember { context.getSharedPreferences("aguatorio_prefs", Context.MODE_PRIVATE) }
    var notificationsEnabled by remember { 
        mutableStateOf(sharedPrefs.getBoolean("notifications_enabled", true))
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentDestination = navController.currentDestination
            )
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
                text = "Configuración",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SettingSection(title = "Preferencias Generales") {
                        // Notificaciones
                        SettingSwitch(
                            title = "Notificaciones",
                            description = "Recordatorios de hidratación",
                            icon = Icons.Default.Notifications,
                            checked = notificationsEnabled,
                            onCheckedChange = { isEnabled ->
                                notificationsEnabled = isEnabled
                                // Guardar la preferencia
                                sharedPrefs.edit().putBoolean("notifications_enabled", isEnabled).apply()
                                
                                if (isEnabled) {
                                    // Reactivar las notificaciones
                                    NotificationUtils.scheduleNotifications(context)
                                } else {
                                    // Cancelar todas las notificaciones programadas
                                    WorkManager.getInstance(context).cancelAllWork()
                                }
                            }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Modo Oscuro
                        SettingSwitch(
                            title = "Modo Oscuro",
                            description = "Cambiar tema de la aplicación",
                            icon = Icons.Default.DarkMode,
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it }
                        )
                    }
                }

                item {
                    SettingSection(title = "Idioma y Región") {
                        // Selector de Idioma
                        SettingDropdown(
                            title = "Idioma",
                            icon = Icons.Default.Language,
                            selectedValue = selectedLanguage,
                            options = listOf("Español", "English", "Português"),
                            onValueChange = { selectedLanguage = it }
                        )
                    }
                }

                item {
                    SettingSection(title = "Objetivos de Hidratación") {
                        // Meta diaria de agua
                        SettingTextField(
                            title = "Meta diaria de agua",
                            icon = Icons.Default.WaterDrop,
                            value = metaAgua,
                            onValueChange = { metaAgua = it }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        // Unidad de medida
                        SettingDropdown(
                            title = "Unidad de medida",
                            icon = Icons.Default.Scale,
                            selectedValue = unidadMedida,
                            options = listOf("ml", "oz", "L"),
                            onValueChange = { unidadMedida = it }
                        )
                    }
                }

                item {
                    SettingSection(title = "Cuenta") {
                        // Botón de cerrar sesión
                        Button(
                            onClick = {
                                // TODO: Implementar cierre de sesión
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar Sesión")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingDropdown(
    title: String,
    icon: ImageVector,
    selectedValue: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    value = selectedValue,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingTextField(
    title: String,
    icon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true
            )
        }
    }
} 