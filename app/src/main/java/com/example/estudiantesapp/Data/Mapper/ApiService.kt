package com.example.estudiantesapp.Data.Mapper

import com.example.estudiantesapp.Domain.model.Estudiantes.Estudiantes
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/Estudiantes")
    suspend fun getEstudiantes(): Response<List<Estudiantes>>

    @GET("api/Estudiantes/{id}")
    suspend fun getEstudiante(@Path("id") id: Int): Response<Estudiantes>

    @POST("api/Estudiantes")
    suspend fun createEstudiante(@Body estudiante: Estudiantes): Response<Estudiantes>

    @PUT("api/Estudiantes/{id}")
    suspend fun updateEstudiante(@Path("id") id: Int, @Body estudiante: Estudiantes): Response<Unit>

    @DELETE("api/Estudiantes/{id}")
    suspend fun deleteEstudiante(@Path("id") id: Int): Response<Unit>
}