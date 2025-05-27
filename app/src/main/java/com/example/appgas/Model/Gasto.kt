package com.example.appgas.Model

data class Gasto(
    val id: Int?,
    val fecha: String,
    val monto: Double,
    val categoria_id: Int,
    val usuario_id: Int
)
