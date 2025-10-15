package com.example.estudiantesapp.Domain.model.Estudiantes

import com.google.gson.annotations.SerializedName

data class Estudiantes(
    @SerializedName("estudianteId")
    val estudianteId: Int = 0,

    @SerializedName("estudianteNombre")
    val estudianteNombre: String = "",

    @SerializedName("estudianteEdad")
    val estudianteEdad: Int = 0,

    @SerializedName("estudianteCarrera")
    val estudianteCarrera: String = "",

    @SerializedName("fecha")
    val fecha: String? = null
)