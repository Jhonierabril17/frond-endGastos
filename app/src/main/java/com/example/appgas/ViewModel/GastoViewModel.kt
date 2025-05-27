package com.example.appgas.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Gasto
import com.example.appgas.Repository.GastoRepository
import com.example.appgas.Service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class GastoViewModel(private val repo: GastoRepository = GastoRepository(RetrofitInstance.api)): ViewModel() {
    private val _gastos = MutableStateFlow<List<Gasto>>(emptyList())
    val gastos: StateFlow<List<Gasto>> = _gastos

    init {
        fetchGastos()
    }

    fun fetchGastos() {
        viewModelScope.launch {
            try {
                val response: Response<List<Gasto>> = repo.getGastos()
                if (response.isSuccessful) {
                    val data = response.body()
                    _gastos.value = data ?: emptyList()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("GastoViewModel", "Error al cargar gastos. Código: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                    _gastos.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("GastoViewModel", "Excepción al cargar gastos: ${e.message}", e)
                _gastos.value = emptyList()
            }
        }
    }

    fun addGasto(gasto: Gasto) {
        viewModelScope.launch {
            try {
                val gastoParaInsertar = gasto.copy(id = null)
                val response: Response<Gasto> = repo.addGasto(gastoParaInsertar)

                if (response.isSuccessful) {
                    fetchGastos()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("GastoViewModel", "Error al añadir gasto: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("GastoViewModel", "Excepción al añadir gasto: ${e.message}", e)
            }
        }
    }

    fun deleteGasto(id: Int) {
        viewModelScope.launch {
            try {
                val success = repo.deleteGasto(id)
                if (success) {
                    fetchGastos()
                } else {
                    Log.e("GastoViewModel", "Fallo al eliminar gasto con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("GastoViewModel", "Excepción al eliminar gasto con ID: $id: ${e.message}", e)
            }
        }
    }
}