package com.example.appgas.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons // Necesario si quieres añadir iconos (ej. Icons.Default.Home)
import androidx.compose.material.icons.filled.ExitToApp // Ejemplo de icono para cerrar sesión
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Bienvenido a AppGas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Botones de navegación principales
                Button(
                    onClick = { navController.navigate("categorias") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gestionar Categorías")
                }
                Button(
                    onClick = { navController.navigate("cuentas") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Administrar Cuentas")
                }
                Button(
                    onClick = { navController.navigate("gastos") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrar Gastos")
                }
                Button(
                    onClick = { navController.navigate("saldos") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Saldos")
                }
                Button(
                    onClick = { navController.navigate("transacciones") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Transacciones")
                }
                Button(
                    onClick = { navController.navigate("reporte_gastos") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reporte de Gastos")
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Botón de Cerrar Sesión ---
        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}