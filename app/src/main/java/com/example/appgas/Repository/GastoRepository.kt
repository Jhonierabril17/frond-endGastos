package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Gasto

class GastoRepository(private val api: ApiService) {
    suspend fun getGastos() = api.getGastos()
    suspend fun addGasto(gasto: Gasto) = api.addGasto(gasto)
    suspend fun deleteGasto(id: Int): Boolean {
        val response = api.deleteGasto("eq.$id")
        return response.isSuccessful
    }
}
