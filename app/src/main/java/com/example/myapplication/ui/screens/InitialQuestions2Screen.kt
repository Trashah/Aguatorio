package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialQuestions2Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var startTime by remember { mutableStateOf(LocalTime.of(6, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(22, 0)) }
    var breakfastTime by remember { mutableStateOf(LocalTime.of(8, 45)) }
    var lunchTime by remember { mutableStateOf(LocalTime.of(12, 45)) }
    var dinnerTime by remember { mutableStateOf(LocalTime.of(20, 0)) }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Barra superior con flecha de retroceso
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )
            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Inicio del día
                Text(
                    text = "¿A qué hora empiezas el día?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de hora de inicio
                OutlinedTextField(
                    value = startTime.format(timeFormatter),
                    onValueChange = { /* Se manejará con un TimePicker */ },
                    label = { Text("Hora de inicio") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Fin del día
                Text(
                    text = "¿A qué hora terminas el día?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de hora de fin
                OutlinedTextField(
                    value = endTime.format(timeFormatter),
                    onValueChange = { /* Se manejará con un TimePicker */ },
                    label = { Text("Hora de fin") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Horarios de comidas
                Text(
                    text = "¿Cuáles son los horarios de tus comidas?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Desayuno
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Desayuno",
                        fontSize = 18.sp
                    )
                    OutlinedTextField(
                        value = breakfastTime.format(timeFormatter),
                        onValueChange = { /* Se manejará con un TimePicker */ },
                        modifier = Modifier.width(150.dp),
                        readOnly = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Almuerzo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Almuerzo",
                        fontSize = 18.sp
                    )
                    OutlinedTextField(
                        value = lunchTime.format(timeFormatter),
                        onValueChange = { /* Se manejará con un TimePicker */ },
                        modifier = Modifier.width(150.dp),
                        readOnly = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cena
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cena",
                        fontSize = 18.sp
                    )
                    OutlinedTextField(
                        value = dinnerTime.format(timeFormatter),
                        onValueChange = { /* Se manejará con un TimePicker */ },
                        modifier = Modifier.width(150.dp),
                        readOnly = true
                    )
                }
            }

            // Botón siguiente
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 18.sp
                )
            }
        }
    }
} 