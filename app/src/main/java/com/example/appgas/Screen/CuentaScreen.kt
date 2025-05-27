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
import com.example.appgas.Model.Cuenta
import com.example.appgas.ViewModel.CuentaViewModel
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CuentaScreen(
    viewModel: CuentaViewModel = viewModel(),
    navController: NavHostController
) {
    val cuentas by viewModel.cuentas.collectAsState()
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var saldo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }

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
            Text("Gestión de Cuentas", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la Cuenta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = saldo,
            onValueChange = { newValue ->
                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    saldo = newValue
                }
            },
            label = { Text("Saldo Inicial") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo de Cuenta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val saldoDouble = saldo.toDoubleOrNull()
                if (nombre.isNotBlank() && saldoDouble != null && tipo.isNotBlank()) {
                    scope.launch {
                        val nuevaCuenta = Cuenta(
                            id = null,
                            nombre = nombre,
                            saldo_inicial = saldoDouble,
                            tipo = tipo,
                            usuario_id = 1
                        )
                        Log.d("CuentaScreen", "Intentando agregar cuenta: $nuevaCuenta")
                        viewModel.addCuenta(nuevaCuenta)

                        nombre = ""
                        saldo = ""
                        tipo = ""
                    }
                } else {
                    Log.d("CuentaScreen", "Por favor, rellena todos los campos para la cuenta.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Agregar Cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Cuentas Existentes", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(cuentas) { cuenta ->
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("ID: ${cuenta.id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                            Text("Nombre: ${cuenta.nombre}", style = MaterialTheme.typography.bodyLarge)
                            Text("Tipo: ${cuenta.tipo}", style = MaterialTheme.typography.bodyMedium)
                            Text("Saldo: $${"%.2f".format(cuenta.saldo_inicial)}", style = MaterialTheme.typography.bodyMedium) // Formato para el saldo
                            Text("Usuario ID: ${cuenta.usuario_id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                        }

                        // Botón para eliminar cuenta
                        IconButton(onClick = {
                            cuenta.id?.let { idToDelete ->
                                Log.d("CuentaScreen", "Intentando eliminar cuenta con ID: $idToDelete")
                                viewModel.deleteCuenta(idToDelete)
                            } ?: run {
                                Log.e("CuentaScreen", "Intento de eliminar una cuenta con ID nulo. Esto no debería ocurrir para cuentas existentes.")
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