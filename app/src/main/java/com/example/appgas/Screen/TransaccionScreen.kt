package com.example.appgas.Screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgas.Model.Transaccion
import com.example.appgas.ViewModel.TransaccionViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.util.Log // ¡Importante: Importar Log para depuración!

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransaccionScreen(viewModel: TransaccionViewModel = viewModel()) {
    val transacciones by viewModel.transacciones.collectAsState()
    val scope = rememberCoroutineScope()

    var fechaInicioFilter by remember { mutableStateOf("") }
    var fechaFinFilter by remember { mutableStateOf("") }

    var newMonto by remember { mutableStateOf("") }
    var newFecha by remember { mutableStateOf("") }
    var newTipo by remember { mutableStateOf("") }
    val fixedUsuarioId = 1


    val transaccionesFiltradas = remember(transacciones, fechaInicioFilter, fechaFinFilter) {
        Log.d("TransaccionScreen", "Aplicando filtros. Inicio: '$fechaInicioFilter', Fin: '$fechaFinFilter'")
        transacciones.filter { transaccion ->
            try {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val transaccionDate = LocalDate.parse(transaccion.fecha, formatter)
                Log.d("TransaccionScreen", "Transacción fecha: ${transaccion.fecha} -> $transaccionDate")

                val startDate = if (fechaInicioFilter.isNotBlank()) LocalDate.parse(fechaInicioFilter, formatter) else null
                val endDate = if (fechaFinFilter.isNotBlank()) LocalDate.parse(fechaFinFilter, formatter) else null

                val matchesStartDate = startDate == null || !transaccionDate.isBefore(startDate)
                val matchesEndDate = endDate == null || !transaccionDate.isAfter(endDate)

                Log.d("TransaccionScreen", "Transacción ID ${transaccion.id}: matchesStartDate=$matchesStartDate, matchesEndDate=$matchesEndDate")
                matchesStartDate && matchesEndDate
            } catch (e: Exception) {
                Log.e("TransaccionScreen", "Error al parsear la fecha de la transacción ID: ${transaccion.id}, Fecha: ${transaccion.fecha}. Error: ${e.message}")
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Gestión de Transacciones", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Sección de Añadir Transacción ---
        Text("Añadir Nueva Transacción", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newMonto,
            onValueChange = { newValue -> newMonto = newValue.filter { it.isDigit() || it == '.' } },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
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
            value = newTipo,
            onValueChange = { newTipo = it },
            label = { Text("Tipo (ingreso/egreso)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val montoDouble = newMonto.toDoubleOrNull()
                Log.d("TransaccionScreen", "Botón 'Guardar Transacción' presionado.")
                Log.d("TransaccionScreen", "Datos ingresados: Monto='$newMonto', Fecha='$newFecha', Tipo='$newTipo'")

                if (newFecha.isNotBlank() && montoDouble != null && montoDouble > 0 && newTipo.isNotBlank()) {
                    scope.launch {
                        val transaccionToAdd = Transaccion(
                            id = 0, // Marcador de posición
                            monto = montoDouble,
                            fecha = newFecha,
                            tipo = newTipo,
                            usuario_id = fixedUsuarioId
                        )
                        Log.d("TransaccionScreen", "Intentando añadir transacción: $transaccionToAdd")
                        viewModel.addTransaccion(transaccionToAdd)
                        // Limpiar campos de entrada después de añadir
                        newMonto = ""
                        newFecha = ""
                        newTipo = ""
                        Log.d("TransaccionScreen", "Campos de entrada limpiados.")
                    }
                } else {
                    Log.d("TransaccionScreen", "Validación fallida: Por favor, completa todos los campos correctamente.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Guardar Transacción")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Sección de Filtros ---
        Text("Filtrar Transacciones", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fechaInicioFilter,
            onValueChange = {
                fechaInicioFilter = it
                Log.d("TransaccionScreen", "Filtro Fecha Inicio cambiado a: '$it'")
            },
            label = { Text("Fecha inicio (yyyy-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fechaFinFilter,
            onValueChange = {
                fechaFinFilter = it
                Log.d("TransaccionScreen", "Filtro Fecha Fin cambiado a: '$it'")
            },
            label = { Text("Fecha fin (yyyy-MM-DD)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Lista de Transacciones ---
        Text("Lista de Transacciones", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (transaccionesFiltradas.isEmpty() && transacciones.isNotEmpty()) {
            Log.d("TransaccionScreen", "Lista filtrada vacía, pero hay transacciones (posiblemente por filtros).")
            Text("No se encontraron transacciones con los filtros aplicados.", style = MaterialTheme.typography.bodySmall)
        } else if (transacciones.isEmpty()) {
            Log.d("TransaccionScreen", "Lista de transacciones completamente vacía.")
            Text("No hay transacciones registradas.", style = MaterialTheme.typography.bodySmall)
        } else {
            Log.d("TransaccionScreen", "Mostrando ${transaccionesFiltradas.size} transacciones filtradas.")
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transaccionesFiltradas) { transaccion ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                                Text(text = "ID: ${transaccion.id}", style = MaterialTheme.typography.bodySmall)
                                Text(text = "Monto: $${transaccion.monto}", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "Fecha: ${transaccion.fecha}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Tipo: ${transaccion.tipo}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Usuario ID: ${transaccion.usuario_id}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(
                                onClick = {
                                    Log.d("TransaccionScreen", "Botón 'Eliminar' presionado para Transacción ID: ${transaccion.id}")
                                    scope.launch {
                                        viewModel.deleteTransaccion(transaccion.id)
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}