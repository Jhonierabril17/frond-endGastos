package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Cuenta

class CuentaRepository(private val api: ApiService) {
    suspend fun getCuentas() = api.getCuentas()
    suspend fun addCuenta(cuenta: Cuenta) = api.addCuenta(cuenta)
    suspend fun updateCuenta(id: Int, cuenta: Cuenta): Boolean {
        val response = api.updateCuenta("eq.$id", cuenta)
        return response.isSuccessful
    }
    suspend fun deleteCuenta(id: Int): Boolean {
        val response = api.deleteCuenta("eq.$id")
        return response.isSuccessful
    }
}