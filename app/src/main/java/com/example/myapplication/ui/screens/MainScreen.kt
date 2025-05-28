package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.WaterGlass
import androidx.navigation.NavHostController
import com.example.myapplication.ui.components.BottomBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.ui.WaterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: WaterViewModel
) {
    val totalAmount by viewModel.totalAmount.collectAsState()
    val completedGlasses by viewModel.completedGlasses.collectAsState()
    val maxAmount = 1000
    val increment = 250

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
                text = "Total acumulado: ${totalAmount}ml",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3CCCDC)
            )

            Spacer(modifier = Modifier.height(32.dp))

            WaterGlass(
                progress = (totalAmount % maxAmount).toFloat() / maxAmount,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (completedGlasses > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WaterGlass(
                        progress = 1f,
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(horizontal = 8.dp)
                    )
                    if (completedGlasses >= 2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            WaterGlass(
                                progress = 1f,
                                modifier = Modifier
                                    .height(100.dp)
                                    .fillMaxWidth()
                            )

                            if (completedGlasses >= 3) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.Yellow,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.TopEnd)
                                        .offset(x = 8.dp, y = (-4).dp)
                                ) {
                                    Text(
                                        text = "+$completedGlasses",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(4.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { viewModel.addWater(increment) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3CCCDC)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir agua",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Añadir ${increment}ml",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}