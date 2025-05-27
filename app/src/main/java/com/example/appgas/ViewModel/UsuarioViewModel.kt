package com.example.appgas.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Usuario
import com.example.appgas.Repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> get() = _usuarios

    private val _usuarioLogueado = MutableStateFlow<Usuario?>(null)
    val usuarioLogueado: StateFlow<Usuario?> get() = _usuarioLogueado

    private val _registroEstado = MutableStateFlow<Boolean?>(null)
    val registroEstado: StateFlow<Boolean?> get() = _registroEstado

    private val _conexionEstado = MutableStateFlow<Boolean?>(null)
    val conexionEstado: StateFlow<Boolean?> get() = _conexionEstado

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val response = repository.getUsuarios()
                if (response.isSuccessful) {
                    _usuarios.value = response.body() ?: emptyList()
                    _conexionEstado.value = true
                    println("DEBUG: Conexión a Supabase exitosa. Código: ${response.code()}")
                } else {
                    _usuarios.value = emptyList()
                    _conexionEstado.value = true
                    println("DEBUG: Conexión a Supabase OK, pero API respondió con error: ${response.code()}")
                }
            } catch (e: HttpException) {
                _usuarios.value = emptyList()
                _conexionEstado.value = false
                println("DEBUG: Error HTTP al conectar a Supabase: ${e.code()} - ${e.message()}")
            } catch (e: IOException) {
                _usuarios.value = emptyList()
                _conexionEstado.value = false
                println("DEBUG: Error de red al conectar a Supabase: ${e.message}")
            } catch (e: Exception) {
                _usuarios.value = emptyList()
                _conexionEstado.value = false
                println("DEBUG: Error inesperado al conectar a Supabase: ${e.message}")
            }
        }
    }

    fun login(usuario: Usuario) {
        viewModelScope.launch {
            _usuarioLogueado.value = null
            try {
                val response = repository.getUsuarios()
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    val usuarioEncontrado = lista.find {
                        it.correo == usuario.correo && it.contrasena == usuario.contrasena
                    }
                    _usuarioLogueado.value = usuarioEncontrado
                    println("DEBUG: Intento de login exitoso. Usuario encontrado: ${usuarioEncontrado != null}")
                } else {
                    _usuarioLogueado.value = null
                    println("DEBUG: Error en respuesta de login: ${response.code()}")
                }
            } catch (e: Exception) {
                _usuarioLogueado.value = null
                println("DEBUG: Excepción durante login: ${e.message}")
            }
        }
    }

    fun addUsuario(usuario: Usuario) {
        viewModelScope.launch {
            _registroEstado.value = null
            try {
                val response = repository.addUsuario(usuario)
                if (response.isSuccessful) {
                    _registroEstado.value = true
                    cargarUsuarios()
                    println("DEBUG: Usuario añadido exitosamente.")
                } else {
                    _registroEstado.value = false

                    val errorBody = response.errorBody()?.string()
                    println("DEBUG: Fallo al añadir usuario (exito=false). Código: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                _registroEstado.value = false
                println("DEBUG: Excepción al añadir usuario: ${e.message}")
            }
        }
    }

    fun deleteUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val exito = repository.deleteUsuario(id)
                if (exito) {
                    cargarUsuarios()
                    println("DEBUG: Usuario con ID $id eliminado exitosamente.")
                } else {
                    println("DEBUG: Fallo al eliminar usuario con ID $id.")
                }
            } catch (e: Exception) {
                println("DEBUG: Excepción al eliminar usuario con ID $id: ${e.message}")
            }
        }
    }

    fun resetRegistroEstado() {
        _registroEstado.value = null
    }

    fun resetConexionEstado() {
        _conexionEstado.value = null
    }
}