package com.example.appgas.Model

data class Cuenta(
    val id: Int?,
    val nombre: String,
    val saldo_inicial: Double,
    val tipo: String,
    val usuario_id: Int?
)