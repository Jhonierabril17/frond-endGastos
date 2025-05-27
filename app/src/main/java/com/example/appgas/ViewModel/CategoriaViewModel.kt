package com.example.appgas.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgas.Model.Categoria
import com.example.appgas.Repository.CategoriaRepository
import com.example.appgas.Service.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoriaViewModel(private val repo: CategoriaRepository = CategoriaRepository(RetrofitInstance.api)): ViewModel() {
    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias

    init {
        Log.d("CategoriaViewModel", "Initializing CategoriaViewModel")
        fetchCategorias()
    }

    fun fetchCategorias() {
        viewModelScope.launch {
            try {
                val response: Response<List<Categoria>> = repo.getCategorias()
                if (response.isSuccessful) {
                    val data = response.body()
                    _categorias.value = data ?: emptyList()
                    Log.d("CategoriaViewModel", "Categorías cargadas: ${_categorias.value.size} items. Datos: $data")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CategoriaViewModel", "Error al cargar categorías. Código: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                    _categorias.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("CategoriaViewModel", "Excepción al cargar categorías: ${e.message}", e)
                Log.e("CategoriaViewModel", Log.getStackTraceString(e))
                _categorias.value = emptyList()
            }
        }
    }

    fun addCategoria(categoria: Categoria) {
        viewModelScope.launch {
            try {
                val categoriaParaInsertar = categoria.copy(id = null)
                val response: Response<Categoria> = repo.addCategorias(categoriaParaInsertar)

                if (response.isSuccessful) {
                    val addedCategoria = response.body()
                    Log.d("CategoriaViewModel", "Categoría añadida exitosamente: $addedCategoria. Recargando categorías.")
                    fetchCategorias()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CategoriaViewModel", "Error al añadir categoría: ${response.code()}, Mensaje: ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("CategoriaViewModel", "Excepción al añadir categoría: ${e.message}", e)
                Log.e("CategoriaViewModel", Log.getStackTraceString(e))
            }
        }
    }
    fun deleteCategoria(id: Int) {
        viewModelScope.launch {
            try {
                val success = repo.deleteCategoria(id)
                if (success) {
                    Log.d("CategoriaViewModel", "Categoría eliminada exitosamente con ID: $id. Recargando categorías.")
                    fetchCategorias()
                } else {
                    Log.e("CategoriaViewModel", "Fallo al eliminar categoría con ID: $id. La API devolvió false.")
                }
            } catch (e: Exception) {
                Log.e("CategoriaViewModel", "Excepción al eliminar categoría con ID: $id: ${e.message}", e)
                Log.e("CategoriaViewModel", Log.getStackTraceString(e))
            }
        }
    }
}