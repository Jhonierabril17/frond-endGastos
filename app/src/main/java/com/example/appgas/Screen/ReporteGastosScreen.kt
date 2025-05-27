package com.example.appgas.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgas.ViewModel.GastoViewModel

@Composable
fun ReporteGastosScreen(viewModel: GastoViewModel = viewModel()) {
    val gastos by viewModel.gastos.collectAsState()


    val resumenPorCategoria = gastos
        .groupBy { it.categoria_id }
        .mapValues { entry ->
            entry.value.sumOf { it.monto }
        }
        .toList()
        .sortedByDescending { it.second }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Reporte de Gastos por Categoría", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))

        if (resumenPorCategoria.isEmpty() && gastos.isNotEmpty()) {
            Text("Cargando reporte o no hay categorías válidas asociadas a los gastos.")
        } else if (resumenPorCategoria.isEmpty()) {
            Text("No hay gastos registrados para generar el reporte.")
        } else {
            LazyColumn {
                items(resumenPorCategoria) { (categoriaId, total) ->
                    Text("Categoría ID: $categoriaId - Total: $${"%.2f".format(total)}")
                }
            }
        }
    }
}