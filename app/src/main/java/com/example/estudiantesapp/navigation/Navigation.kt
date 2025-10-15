package com.example.estudiantesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.estudiantesapp.Domain.model.Estudiantes.Estudiantes
import com.example.estudiantesapp.Presentation.EstudiantesPresentation.EstudiantesViewModel
import com.example.estudiantesapp.Presentation.EstudiantesPresentation.edit.EstudianteEditScreen
import com.example.estudiantesapp.Presentation.EstudiantesPresentation.list.EstudiantesListScreen
import com.google.gson.Gson

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Add : Screen("add")
    object Edit : Screen("edit/{estudiante}") {
        fun createRoute(estudiante: Estudiantes): String {
            val json = Gson().toJson(estudiante)
            return "edit/$json"
        }
    }
}

@Composable
fun NavigationGraph(viewModel: EstudiantesViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            EstudiantesListScreen(
                viewModel = viewModel,
                onNavigateToAdd = {
                    navController.navigate(Screen.Add.route)
                },
                onNavigateToEdit = { estudiante ->
                    navController.navigate(Screen.Edit.createRoute(estudiante))
                }
            )
        }

        composable(Screen.Add.route) {
            EstudianteEditScreen(
                viewModel = viewModel,
                estudiante = null,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("estudiante") { type = NavType.StringType })
        ) { backStackEntry ->
            val estudianteJson = backStackEntry.arguments?.getString("estudiante")
            val estudiante = Gson().fromJson(estudianteJson, Estudiantes::class.java)

            EstudianteEditScreen(
                viewModel = viewModel,
                estudiante = estudiante,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}