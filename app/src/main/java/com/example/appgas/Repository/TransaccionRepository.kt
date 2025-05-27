package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Transaccion
import retrofit2.Response

class TransaccionRepository(private val api: ApiService) {

    suspend fun getTransacciones(): Response<List<Transaccion>> {
        return api.getTransacciones()
    }

    suspend fun addTransaccion(transaccion: Transaccion): Response<Transaccion> {
        return api.addTransaccion(transaccion)
    }

    suspend fun updateTransaccion(id: Int, transaccion: Transaccion): Boolean {
        val response = api.updateTransaccion("eq.$id", transaccion)
        return response.isSuccessful
    }

    suspend fun deleteTransaccion(id: Int): Boolean {
        val response = api.deleteTransaccion("eq.$id")
        return response.isSuccessful
    }
}