package com.example.appgas.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appgas.Model.Usuario
import com.example.appgas.Service.RetrofitInstance
import com.example.appgas.ViewModel.UsuarioViewModel
import com.example.appgas.ViewModel.UsuarioViewModelFactory

@Composable
fun LoginScreen(
    navController: NavHostController,
) {
    val repository = remember { RetrofitInstance.usuarioRepository }

    val factory = remember { UsuarioViewModelFactory(repository) }

    val viewModel: UsuarioViewModel = viewModel(factory = factory)

    val correo = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    val usuarioLogueadoState = viewModel.usuarioLogueado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = correo.value,
            onValueChange = { correo.value = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = contrasena.value,
            onValueChange = { contrasena.value = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
           )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.login(Usuario(0, contrasena.value, correo.value, ""))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        LaunchedEffect(usuarioLogueadoState.value) {
            if (usuarioLogueadoState.value != null) {
              navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}