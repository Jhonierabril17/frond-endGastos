package com.example.appgas.Model

data class Transaccion(
    val id: Int,
    val fecha: String,
    val monto: Double,
    val tipo: String,
    val usuario_id: Int
)
