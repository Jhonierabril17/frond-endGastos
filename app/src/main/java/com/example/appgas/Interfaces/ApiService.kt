package com.example.appgas.Interfaces

import com.example.appgas.Model.Categoria
import com.example.appgas.Model.Cuenta
import com.example.appgas.Model.Gasto
import com.example.appgas.Model.Saldo
import com.example.appgas.Model.Transaccion
import com.example.appgas.Model.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

  @GET("usuario")
  suspend fun login(
    @Query("correo") correo: String,
    @Query("contrasena") contrasena: String,
    @Query("select") select: String = "*"
  ): Response<List<Usuario>>

  // GET para obtener todos los usuarios
  @GET("usuario")
  suspend fun getUsuarios(@Query("select") select: String = "*"): Response<List<Usuario>>

  @Headers("Prefer: return=representation")
  @POST("usuario")
  suspend fun addUsuario(@Body usuario: Usuario): Response<Usuario>

  @DELETE("usuario")
  suspend fun deleteUsuario(@Query("id", encoded = true) id: String): Response<Unit>

  // CATEGORIAS
  @GET("categoria")
  suspend fun getCategorias(@Query("select") select: String = "*"): Response<List<Categoria>>

  @Headers("Prefer: return=representation")
  @POST("categoria")
  suspend fun addCategorias(@Body categoria: Categoria): Response<Categoria>
  @DELETE("categoria")
  suspend fun deleteCategoria(@Query("id", encoded = true) id: String): Response<Unit>

  // CUENTAS
  @GET("cuenta")
  suspend fun getCuentas(@Query("select") select: String = "*"): Response<List<Cuenta>>

  @Headers("Prefer: return=representation")
  @POST("cuenta")
  suspend fun addCuenta(@Body cuenta: Cuenta): Response<Cuenta>

  @Headers("Prefer: return=representation")
  @PUT("cuenta")
  suspend fun updateCuenta(@Query("id", encoded = true) id: String, @Body cuenta: Cuenta): Response<Cuenta>

  @DELETE("cuenta")
  suspend fun deleteCuenta(@Query("id", encoded = true) id: String): Response<Unit>

  // GASTOS
  @GET("gasto")
  suspend fun getGastos(@Query("select") select: String = "*"): Response<List<Gasto>>

  @Headers("Prefer: return=representation")
  @POST("gasto")
  suspend fun addGasto(@Body gasto: Gasto): Response<Gasto>

  @DELETE("gasto")
  suspend fun deleteGasto(@Query("id", encoded = true) id: String): Response<Unit>


  // SALDOS
  @GET("saldo")
  suspend fun getSaldos(@Query("select") select: String = "*"): Response<List<Saldo>>

  @Headers("Prefer: return=representation")
  @POST("saldo")
  suspend fun addSaldo(@Body saldo: Saldo): Response<Saldo>


  @Headers("Prefer: return=representation")
  @PATCH("saldo")
  suspend fun updateSaldo(@Body saldo: Saldo, @Query("id", encoded = true) id: String): Response<Saldo>


  @DELETE("saldo")
  suspend fun deleteSaldo(@Query("id", encoded = true) id: String): Response<Unit>

  @GET("transaccion")
  suspend fun getTransacciones(@Query("select") select: String = "*"): Response<List<Transaccion>>

  @Headers("Prefer: return=representation")
  @POST("transaccion")
  suspend fun addTransaccion(@Body transaccion: Transaccion): Response<Transaccion>

  @Headers("Prefer: return=representation")
  @PUT("transaccion")
  suspend fun updateTransaccion(@Query("id", encoded = true) id: String, @Body transaccion: Transaccion): Response<Transaccion>

  @DELETE("transaccion")
  suspend fun deleteTransaccion(@Query("id", encoded = true) id: String): Response<Unit>
}