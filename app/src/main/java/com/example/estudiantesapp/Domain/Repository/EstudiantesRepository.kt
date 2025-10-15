package com.example.estudiantesapp.Domain.Repository

import com.example.estudiantesapp.Data.Mapper.InstanciaRetrofit
import com.example.estudiantesapp.Domain.model.Estudiantes.Estudiantes
import retrofit2.Response

class EstudiantesRepository {
    private val api = InstanciaRetrofit.api

    suspend fun getEstudiantes(): Response<List<Estudiantes>> {
        return api.getEstudiantes()
    }

    suspend fun getEstudiante(id: Int): Response<Estudiantes> {
        return api.getEstudiante(id)
    }

    suspend fun createEstudiante(estudiante: Estudiantes): Response<Estudiantes> {
        return api.createEstudiante(estudiante)
    }

    suspend fun updateEstudiante(id: Int, estudiante: Estudiantes): Response<Unit> {
        return api.updateEstudiante(id, estudiante)
    }

    suspend fun deleteEstudiante(id: Int): Response<Unit> {
        return api.deleteEstudiante(id)
    }
}