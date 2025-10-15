package com.example.estudiantesapp.Presentation.EstudiantesPresentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.estudiantesapp.Domain.Repository.EstudiantesRepository
import com.example.estudiantesapp.Domain.model.Estudiantes.Estudiantes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstudiantesViewModel : ViewModel() {
    private val repository = EstudiantesRepository()

    private val _estudiantes = MutableStateFlow<List<Estudiantes>>(emptyList())
    val estudiantes: StateFlow<List<Estudiantes>> = _estudiantes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadEstudiantes()
    }

    fun loadEstudiantes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            android.util.Log.d("EstudiantesViewModel", "Iniciando carga de estudiantes...")
            try {
                val response = repository.getEstudiantes()
                android.util.Log.d("EstudiantesViewModel", "Response code: ${response.code()}")
                android.util.Log.d("EstudiantesViewModel", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    android.util.Log.d("EstudiantesViewModel", "Estudiantes recibidos: ${data.size}")
                    _estudiantes.value = data
                } else {
                    val errorMsg = "Error al cargar estudiantes: ${response.code()} - ${response.message()}"
                    android.util.Log.e("EstudiantesViewModel", errorMsg)
                    _error.value = errorMsg
                }
            } catch (e: Exception) {
                val errorMsg = "Error de conexión: ${e.message}"
                android.util.Log.e("EstudiantesViewModel", errorMsg, e)
                _error.value = errorMsg
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createEstudiante(estudiante: Estudiantes, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.createEstudiante(estudiante)
                if (response.isSuccessful) {
                    loadEstudiantes()
                    onSuccess()
                } else {
                    _error.value = "Error al crear estudiante: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateEstudiante(estudiante: Estudiantes, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.updateEstudiante(estudiante.estudianteId, estudiante)
                if (response.isSuccessful) {
                    loadEstudiantes()
                    onSuccess()
                } else {
                    _error.value = "Error al actualizar estudiante: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEstudiante(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.deleteEstudiante(id)
                if (response.isSuccessful) {
                    loadEstudiantes()
                } else {
                    _error.value = "Error al eliminar estudiante: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}