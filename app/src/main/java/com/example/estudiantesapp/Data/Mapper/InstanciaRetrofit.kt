package com.example.estudiantesapp.Data.Mapper

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object InstanciaRetrofit {

    private const val BASE_URL = "https://estudiantesapi.somee.com/"

    init {
        Log.d("InstanciaRetrofit", "BASE_URL configurada: $BASE_URL")
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request()
            Log.d("InstanciaRetrofit", "URL completa: ${request.url}")
            try {
                val response = chain.proceed(request)
                Log.d("InstanciaRetrofit", "Response code: ${response.code}")
                response
            } catch (e: Exception) {
                Log.e("InstanciaRetrofit", "Error en request: ${e.message}", e)
                throw e
            }
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}