package com.example.appgas.Model

import com.google.gson.annotations.SerializedName

data class Saldo(
    val id: Int? = null,
    val fecha: String,
    val monto: Double,
    val cuenta_id: Int,
    val usuario_id: Int
)