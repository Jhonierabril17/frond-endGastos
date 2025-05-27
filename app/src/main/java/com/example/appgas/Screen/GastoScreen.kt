package com.example.appgas.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appgas.Model.Gasto
import com.example.appgas.ViewModel.GastoViewModel
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun GastoScreen(
    viewModel: GastoViewModel = viewModel(),
    navController: NavHostController
) {
    val gastos by viewModel.gastos.collectAsState()
    val scope = rememberCoroutineScope()

    var newGastoFecha by remember { mutableStateOf("") }
    var newGastoMonto by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text("Gestión de Gastos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newGastoFecha,
            onValueChange = { newGastoFecha = it },
            label = { Text("Fecha del Gasto (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newGastoMonto,
            onValueChange = { newValue ->
                newGastoMonto = newValue.filter { it.isDigit() || it == '.' }
            },
            label = { Text("Monto del Gasto") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val montoDouble = newGastoMonto.toDoubleOrNull()
                if (newGastoFecha.isNotBlank() && montoDouble != null && montoDouble > 0) {
                    scope.launch {
                        viewModel.addGasto(
                            Gasto(
                                id = null,
                                fecha = newGastoFecha,
                                monto = montoDouble,
                                categoria_id = 1,
                                usuario_id = 1
                            )
                        )
                        newGastoFecha = ""
                        newGastoMonto = ""
                    }
                } else {
                    Log.d("GastoScreen", "Por favor, ingresa una fecha y un monto válido para el gasto.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Agregar Gasto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Gastos Registrados", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gastos) { gasto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(text = "ID: ${gasto.id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Fecha: ${gasto.fecha}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Monto: $${"%.2f".format(gasto.monto)}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "Categoría ID: ${gasto.categoria_id}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Usuario ID: ${gasto.usuario_id}", style = MaterialTheme.typography.bodySmall)
                        }

                        IconButton(onClick = {
                            gasto.id?.let { idToDelete ->
                                Log.d("GastoScreen", "Intentando eliminar gasto con ID: $idToDelete")
                                viewModel.deleteGasto(idToDelete)
                            } ?: run {
                                Log.e("GastoScreen", "Intento de eliminar un gasto con ID nulo. Esto no debería ocurrir para gastos existentes.")
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}