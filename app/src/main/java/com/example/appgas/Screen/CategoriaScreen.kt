package com.example.appgas.Screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appgas.Model.Categoria
import com.example.appgas.ViewModel.CategoriaViewModel
import kotlinx.coroutines.launch
import android.util.Log

@Composable
fun CategoriaScreen(
    viewModel: CategoriaViewModel = viewModel(),
    navController: NavHostController
) {
    val categorias by viewModel.categorias.collectAsState()
    var newCategoryName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
            Text("Gestión de Categorías", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newCategoryName,
            onValueChange = { newCategoryName = it },
            label = { Text("Nombre de la nueva categoría") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (newCategoryName.isNotBlank()) {
                    scope.launch {
                        viewModel.addCategoria(Categoria(id = null, nombre = newCategoryName))
                        newCategoryName = ""
                    }
                } else {
                    Log.d("CategoriaScreen", "El nombre de la categoría no puede estar vacío.")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Agregar Categoría")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Categorías Existentes", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categorias) { categoria ->
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
                            Text("Nombre: ${categoria.nombre}", style = MaterialTheme.typography.bodyLarge)
                            Text("ID: ${categoria.id ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                        }
                        // --- Botón de Eliminar Categoría ---
                        IconButton(onClick = {
                            categoria.id?.let { idToDelete ->
                                Log.d("CategoriaScreen", "Intentando eliminar categoría con ID: $idToDelete")
                                viewModel.deleteCategoria(idToDelete)
                            } ?: run {
                                Log.e("CategoriaScreen", "Intento de eliminar una categoría con ID nulo.")
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