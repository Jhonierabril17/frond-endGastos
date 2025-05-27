package com.example.appgas.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Cuenta
import com.example.appgas.Repository.CuentaRepository
import com.example.appgas.Service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CuentaViewModel(private val repo: CuentaRepository = CuentaRepository(RetrofitInstance.api)): ViewModel() {
    private val _cuentas = MutableStateFlow<List<Cuenta>>(emptyList())
    val cuentas: StateFlow<List<Cuenta>> = _cuentas

    init {
        Log.d("CuentaViewModel", "Initializing CuentaViewModel")
        fetchCuentas()
    }


    private fun fetchCuentas() {
        viewModelScope.launch {
            try {
                val response = repo.getCuentas()
                if (response.isSuccessful) {
                    val data = response.body()
                    _cuentas.value = data ?: emptyList()
                    Log.d("CuentaViewModel", "Cuentas cargadas: ${_cuentas.value.size} items. Datos: $data")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CuentaViewModel", "Error al cargar cuentas. Código: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                    _cuentas.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("CuentaViewModel", "Excepción al cargar cuentas: ${e.message}", e)
                _cuentas.value = emptyList()
            }
        }
    }

    fun addCuenta(cuenta: Cuenta) {
        viewModelScope.launch {
            try {
                val response = repo.addCuenta(cuenta)
                if (response.isSuccessful) {
                    val addedCuenta = response.body()
                    Log.d("CuentaViewModel", "Cuenta añadida exitosamente: $addedCuenta. Recargando cuentas.")
                    fetchCuentas() // Recargar la lista
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CuentaViewModel", "Error al añadir cuenta: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("CuentaViewModel", "Excepción al añadir cuenta: ${e.message}", e)
            }
        }
    }

    fun updateCuenta(id: Int, cuenta: Cuenta) {
        viewModelScope.launch {
            try {
                val success = repo.updateCuenta(id, cuenta)
                if (success) {
                    Log.d("CuentaViewModel", "Cuenta actualizada exitosamente. Recargando cuentas.")
                    fetchCuentas() // Recargar la lista
                } else {
                    Log.e("CuentaViewModel", "Fallo al actualizar cuenta con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("CuentaViewModel", "Excepción al actualizar cuenta: ${e.message}", e)
            }
        }
    }

    fun deleteCuenta(id: Int) {
        viewModelScope.launch {
            try {
                val success = repo.deleteCuenta(id)
                if (success) {
                    Log.d("CuentaViewModel", "Cuenta eliminada exitosamente. Recargando cuentas.")
                    fetchCuentas()
                } else {
                     Log.e("CuentaViewModel", "Fallo al eliminar cuenta con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("CuentaViewModel", "Excepción al eliminar cuenta: ${e.message}", e)
            }
        }
    }
}