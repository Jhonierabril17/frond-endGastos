package com.example.appgas.Repository

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Model.Saldo

class SaldoRepository(private val api: ApiService) {
    suspend fun getSaldos() = api.getSaldos()
    suspend fun addSaldo(saldo: Saldo) = api.addSaldo(saldo)
    suspend fun updateSaldo(id: Int, saldo: Saldo): Boolean {
        val response = api.updateSaldo(saldo, "eq.$id")
        return response.isSuccessful
    }
    suspend fun deleteSaldo(id: Int): Boolean {
        val response = api.deleteSaldo("eq.$id")
        return response.isSuccessful
    }

}