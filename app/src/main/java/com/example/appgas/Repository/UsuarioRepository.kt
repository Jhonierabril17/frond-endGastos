package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Usuario
import retrofit2.Response

class UsuarioRepository(private val apiService: ApiService) {

    suspend fun getUsuarios(): Response<List<Usuario>> {
        return apiService.getUsuarios()
    }

    suspend fun addUsuario(usuario: Usuario): Response<Usuario> {
        return apiService.addUsuario(usuario)
    }

    suspend fun deleteUsuario(id: Int): Boolean {
        val response = apiService.deleteUsuario("eq.$id")
        return response.isSuccessful
    }
}