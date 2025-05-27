package com.example.appgas.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Transaccion
import com.example.appgas.Repository.TransaccionRepository
import com.example.appgas.Service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransaccionViewModel(private val repo: TransaccionRepository = TransaccionRepository(RetrofitInstance.api)) : ViewModel() {
    private val _transacciones = MutableStateFlow<List<Transaccion>>(emptyList())
    val transacciones: StateFlow<List<Transaccion>> = _transacciones

    init {
        viewModelScope.launch {
            _transacciones.value = repo.getTransacciones().body() ?: emptyList()
        }
    }

    fun addTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            repo.addTransaccion(transaccion)
            _transacciones.value = repo.getTransacciones().body() ?: emptyList()
        }
    }

    fun updateTransaccion(id: Int, transaccion: Transaccion) {
        viewModelScope.launch {
            repo.updateTransaccion(id, transaccion)
            _transacciones.value = repo.getTransacciones().body() ?: emptyList()
        }
    }

    fun deleteTransaccion(id: Int) {
        viewModelScope.launch {
            repo.deleteTransaccion(id)
            _transacciones.value = repo.getTransacciones().body() ?: emptyList()
        }
    }
}