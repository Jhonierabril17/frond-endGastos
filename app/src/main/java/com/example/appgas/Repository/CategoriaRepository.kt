package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Categoria
import retrofit2.Response

class CategoriaRepository(private val api: ApiService) {
    suspend fun getCategorias(): Response<List<Categoria>> = api.getCategorias()
    suspend fun addCategorias(categoria: Categoria): Response<Categoria> = api.addCategorias(categoria)
    suspend fun deleteCategoria(id: Int): Boolean {
        val response = api.deleteCategoria("eq.$id")
        return response.isSuccessful
    }
}