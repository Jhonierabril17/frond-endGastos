package com.example.appgas.Screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appgas.Model.Saldo
import com.example.appgas.ViewModel.SaldoViewModel
import kotlinx.coroutines.launch

@Composable
fun SaldoScreen(
    viewModel: SaldoViewModel = viewModel(),
    navController: NavHostController
) {
    val saldos by viewModel.saldos.collectAsState()
    val scope = rememberCoroutineScope()

    // Estados para añadir nuevo saldo
    var newFecha by remember { mutableStateOf("") }
    var newMonto by remember { mutableStateOf("") }
    val fixedAccountId = 1
    val fixedUserId = 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Barra superior con título y botón de retroceso ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Gestión de Saldos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // --- Sección para Añadir Saldo ---
        Text("Añadir Nuevo Saldo", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newFecha,
            onValueChange = { newFecha = it },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newMonto,
            onValueChange = { newValue ->
                newMonto = newValue.filter { it.isDigit() || it == '.' }
            },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val montoDouble = newMonto.toDoubleOrNull()
                if (newFecha.isNotBlank() && montoDouble != null && montoDouble >= 0) {
                    scope.launch {
                        val nuevoSaldo = Saldo(
                            id = null,
                            fecha = newFecha,
                            monto = montoDouble,
                            cuenta_id = fixedAccountId,
                            usuario_id = fixedUserId
                        )
                        Log.d("SaldoScreen", "Intentando agregar saldo: $nuevoSaldo")
                        viewModel.addSaldo(nuevoSaldo)
                        newFecha = ""
                        newMonto = ""
                    }
                } else {
                    Log.d("SaldoScreen", "Por favor, completa la fecha y un monto válido para el nuevo saldo.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Agregar Saldo")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text("Saldos Existentes", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (saldos.isEmpty()) {
            Text("No hay saldos registrados.", style = MaterialTheme.typography.bodySmall)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(saldos) { saldo ->
                    var nuevoMontoEdit by remember { mutableStateOf(saldo.monto.toString()) }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Fecha: ${saldo.fecha}", style = MaterialTheme.typography.bodyLarge)
                            Text("Monto actual: $${"%.2f".format(saldo.monto)}", style = MaterialTheme.typography.bodyMedium)
                            Text("Cuenta ID: ${saldo.cuenta_id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                            Text("Usuario ID: ${saldo.usuario_id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nuevoMontoEdit,
                                onValueChange = { newValue ->
                                    nuevoMontoEdit = newValue.filter { it.isDigit() || it == '.' }
                                },
                                label = { Text("Nuevo Monto") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            saldo.id?.let { idToUpdate ->
                                                val montoActualizado = nuevoMontoEdit.toDoubleOrNull() ?: saldo.monto
                                                val saldoActualizado = saldo.copy(monto = montoActualizado)
                                                Log.d("SaldoScreen", "Actualizando saldo ID: $idToUpdate a monto: $montoActualizado")
                                                viewModel.updateSaldo(idToUpdate, saldoActualizado)
                                            } ?: run {
                                                Log.e("SaldoScreen", "Intento de actualizar un saldo con ID nulo.")
                                            }
                                        }
                                    },
                                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("Actualizar")
                                }

                                // Botón de eliminar
                                IconButton(onClick = {
                                    saldo.id?.let { idToDelete ->
                                        Log.d("SaldoScreen", "Eliminando saldo con ID: $idToDelete")
                                        viewModel.deleteSaldo(idToDelete)
                                    } ?: run {
                                        Log.e("SaldoScreen", "Intento de eliminar un saldo con ID nulo.")
                                    }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Saldo", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}