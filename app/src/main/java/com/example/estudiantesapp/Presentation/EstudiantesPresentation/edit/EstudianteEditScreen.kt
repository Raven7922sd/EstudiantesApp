package com.example.estudiantesapp.Presentation.EstudiantesPresentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.estudiantesapp.Domain.model.Estudiantes.Estudiantes
import com.example.estudiantesapp.Presentation.EstudiantesPresentation.EstudiantesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteEditScreen(
    viewModel: EstudiantesViewModel,
    estudiante: Estudiantes? = null,
    onNavigateBack: () -> Unit
) {
    var nombre by remember { mutableStateOf(estudiante?.estudianteNombre ?: "") }
    var edad by remember { mutableStateOf(estudiante?.estudianteEdad?.toString() ?: "") }
    var carrera by remember { mutableStateOf(estudiante?.estudianteCarrera ?: "") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val isEditing = estudiante != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Estudiante" else "Agregar Estudiante") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mostrar error si existe
            error?.let {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Estudiante") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true
            )

            OutlinedTextField(
                value = edad,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        edad = it
                    }
                },
                label = { Text("Edad") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = carrera,
                onValueChange = { carrera = it },
                label = { Text("Carrera") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val edadInt = edad.toIntOrNull() ?: 0

                    if (nombre.isNotBlank() && edadInt in 1..90 && carrera.isNotBlank()) {
                        val estudianteToSave = Estudiantes(
                            estudianteId = estudiante?.estudianteId ?: 0,
                            estudianteNombre = nombre,
                            estudianteEdad = edadInt,
                            estudianteCarrera = carrera
                        )

                        if (isEditing) {
                            viewModel.updateEstudiante(estudianteToSave) {
                                onNavigateBack()
                            }
                        } else {
                            viewModel.createEstudiante(estudianteToSave) {
                                onNavigateBack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading &&
                        nombre.isNotBlank() &&
                        (edad.toIntOrNull() ?: 0) in 1..90 &&
                        carrera.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (isEditing) "Actualizar" else "Guardar")
                }
            }

            Text(
                text = "Todos los campos son obligatorios\nLa edad debe estar entre 1 y 90 años",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
