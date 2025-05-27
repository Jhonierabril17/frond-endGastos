package com.example.appgas.Screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appgas.Model.Usuario
import com.example.appgas.Repository.UsuarioRepository
import com.example.appgas.Service.RetrofitInstance.api
import com.example.appgas.ViewModel.UsuarioViewModel
import com.example.appgas.ViewModel.UsuarioViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun UsuarioScreen() {
    val repository = remember { UsuarioRepository(api) }
    val factory = remember { UsuarioViewModelFactory(repository) }
    val usuarioViewModel: UsuarioViewModel = viewModel(factory = factory)
    val usuarios by usuarioViewModel.usuarios.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Usuarios", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && correo.isNotBlank() && contrasena.isNotBlank()) {
                    scope.launch {
                        usuarioViewModel.addUsuario(Usuario(null, contrasena, correo, nombre))
                        nombre = ""
                        correo = ""
                        contrasena = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Usuario")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(usuarios) { usuario ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation()
                ) {
                    Row(
                        Modifier
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Nombre: ${usuario.nombre}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Correo: ${usuario.correo}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch {

                                    usuario.id.let { idToDelete ->
                                        Log.d("UsuarioScreen", "Intentando eliminar usuario con ID: $idToDelete")
                                        if (idToDelete != null) {
                                            usuarioViewModel.deleteUsuario(idToDelete)
                                        }
                                    } ?: run {
                                        Log.e("UsuarioScreen", "Intento de eliminar un usuario con ID nulo. Esto no debería ocurrir para usuarios existentes.")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}
