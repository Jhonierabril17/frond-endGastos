package com.example.appgas.Service

import com.example.appgas.Interfaces.ApiService
import com.example.appgas.Repository.UsuarioRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://gtmmpciazcwnzlsjqfle.supabase.co/rest/v1/"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd0bW1wY2lhemN3bnpsc2pxZmxlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc3ODY4NzMsImV4cCI6MjA2MzM2Mjg3M30.dUDZ3mmTUbLWSZRIqpm5B-_7xn6dxHsxpUQRMVN0iEA" // Obtén esta clave de tu Dashboard de Supabase (Configuración del proyecto -> API)

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("apikey", SUPABASE_ANON_KEY)
            .header("Authorization", "Bearer $SUPABASE_ANON_KEY")
            .header("Content-Type", "application/json")
            .build()
        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(api)
    }
}