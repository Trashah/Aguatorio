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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialQuestions1Screen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    var selectedSexo by remember { mutableStateOf<String?>(null) }
    var peso by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var selectedClima by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference

    fun saveDataToFirebase() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            isLoading = true
            errorMessage = null

            val userProfile = mapOf(
                "sexo" to selectedSexo,
                "peso" to peso,
                "edad" to edad,
                "clima" to selectedClima
            )

            database.child("usuarios").child(userId).child("respuestas").child("biometria").setValue(userProfile)
                .addOnSuccessListener {
                    isLoading = false
                    onNextClick() // Navigate to the next screen
                }
                .addOnFailureListener { error ->
                    isLoading = false
                    errorMessage = "Error: ${'$'}{error.message}"
                }
        } else {
            errorMessage = "User not authenticated"
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "¿Cuál es tu sexo?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Hombre", "Mujer", "Otro").forEach { sexo ->
                        Button(
                            onClick = { selectedSexo = sexo },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedSexo == sexo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        ) { Text(text = sexo) }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "¿Cuál es tu peso?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso en kg") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "¿Cuál es tu edad?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "¿Cómo es el clima en tu área?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Caliente", "Templado", "Frío").forEach { clima ->
                        Button(
                            onClick = { selectedClima = clima },
                            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedClima == clima) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        ) { Text(text = clima) }
                    }
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }

            Button(
                onClick = { saveDataToFirebase() },
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = selectedSexo != null && peso.isNotEmpty() && edad.isNotEmpty() && selectedClima != null && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = "Siguiente", fontSize = 18.sp)
                }
            }
        }
    }
}
