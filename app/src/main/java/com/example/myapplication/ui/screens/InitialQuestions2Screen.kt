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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Botón de retroceso
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Inicio del día
                Text("¿A qué hora empiezas el día?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = startTime.format(timeFormatter),
                    onValueChange = {},
                    label = { Text("Hora de inicio") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Fin del día
                Text("¿A qué hora terminas el día?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = endTime.format(timeFormatter),
                    onValueChange = {},
                    label = { Text("Hora de fin") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Horarios de comidas
                Text("¿Cuáles son los horarios de tus comidas?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                listOf(
                    "Desayuno" to breakfastTime,
                    "Almuerzo" to lunchTime,
                    "Cena" to dinnerTime
                ).forEach { (label, time) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, fontSize = 18.sp)
                        OutlinedTextField(
                            value = time.format(timeFormatter),
                            onValueChange = {},
                            modifier = Modifier.width(150.dp),
                            readOnly = true
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator()
                }
            }

            // Botón siguiente con guardado en Firebase
            Button(
                onClick = {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        isLoading = true
                        val scheduleData = mapOf(
                            "horaInicio" to startTime.format(timeFormatter),
                            "horaFin" to endTime.format(timeFormatter),
                            "horaDesayuno" to breakfastTime.format(timeFormatter),
                            "horaComida" to lunchTime.format(timeFormatter),
                            "horaCena" to dinnerTime.format(timeFormatter)
                        )

                        database.child("usuarios").child(userId).child("respuestas").child("horario")
                            .setValue(scheduleData)
                            .addOnSuccessListener {
                                isLoading = false
                                errorMessage = null
                                onNextClick()
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                errorMessage = "Error al guardar: ${e.message}"
                            }
                    } else {
                        errorMessage = "Usuario no autenticado."
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !isLoading
            ) {
                Text("Siguiente", fontSize = 18.sp)
            }
        }
    }
}
