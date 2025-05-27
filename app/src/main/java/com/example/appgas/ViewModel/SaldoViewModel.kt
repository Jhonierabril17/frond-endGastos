package com.example.appgas.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Saldo
import com.example.appgas.Repository.SaldoRepository
import com.example.appgas.Service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SaldoViewModel(private val repo: SaldoRepository = SaldoRepository(RetrofitInstance.api)) : ViewModel() {
    private val _saldos = MutableStateFlow<List<Saldo>>(emptyList())
    val saldos: StateFlow<List<Saldo>> = _saldos

    init {
        Log.d("SaldoViewModel", "Initializing SaldoViewModel")
        fetchSaldos()
    }

    fun fetchSaldos() {
        viewModelScope.launch {
            try {
                val response: Response<List<Saldo>> = repo.getSaldos() // Especificar tipo para claridad
                if (response.isSuccessful) {
                    val data = response.body()
                    _saldos.value = data ?: emptyList()
                    Log.d("SaldoViewModel", "Saldos cargados: ${_saldos.value.size} items. Datos: $data")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SaldoViewModel", "Error al cargar saldos. Código: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                    _saldos.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("SaldoViewModel", "Excepción al cargar saldos: ${e.message}", e)
                Log.e("SaldoViewModel", Log.getStackTraceString(e))
                _saldos.value = emptyList()
            }
        }
    }

    fun addSaldo(saldo: Saldo) {
        viewModelScope.launch {
            try {
                val response: Response<Saldo> = repo.addSaldo(saldo) // Especificar tipo para claridad
                if (response.isSuccessful) {
                    Log.d("SaldoViewModel", "Saldo añadido exitosamente. Recargando saldos.")
                    fetchSaldos() // Recargar después de añadir
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("SaldoViewModel", "Error al añadir saldo: ${response.code()} - ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("SaldoViewModel", "Excepción al añadir saldo: ${e.message}", e)
                Log.e("SaldoViewModel", Log.getStackTraceString(e))
            }
        }
    }

    fun updateSaldo(id: Int, saldo: Saldo) {
        viewModelScope.launch {
            try {
                val success: Boolean = repo.updateSaldo(id, saldo)
                if (success) {
                    Log.d("SaldoViewModel", "Saldo actualizado exitosamente. Recargando saldos.")
                    fetchSaldos()
                } else {
                    Log.e("SaldoViewModel", "Fallo al actualizar saldo con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("SaldoViewModel", "Excepción al actualizar saldo: ${e.message}", e)
                Log.e("SaldoViewModel", Log.getStackTraceString(e))
            }
        }
    }

    fun deleteSaldo(id: Int) {
        viewModelScope.launch {
            try {
                val success = repo.deleteSaldo(id)
                if (success) {
                    Log.d("SaldoViewModel", "Saldo eliminado exitosamente con ID: $id. Recargando saldos.")
                    fetchSaldos()
                } else {
                    Log.e("SaldoViewModel", "Fallo al eliminar saldo con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("SaldoViewModel", "Excepción al eliminar saldo con ID: $id: ${e.message}", e)
                Log.e("SaldoViewModel", Log.getStackTraceString(e))
            }
        }
    }
}