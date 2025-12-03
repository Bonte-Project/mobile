package ua.nure.bonte.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.bonte.ui.trainer.edit.TrainerScreen
import ua.nure.bonte.ui.trainer.list.TrainerListScreen

fun NavGraphBuilder.trainerGraph(navController: NavController) {
    navigation<NestedGraph.Trainer>(
        startDestination = Screen.Trainer.TrainerList
    ) {
        composable<Screen.Trainer.TrainerProfile> {
            TrainerScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Trainer.TrainerList> {
            TrainerListScreen(
                viewModel = hiltViewModel(),
                onBack = {
                    navController.navigateUp()
                },
                onNavigate = {
                    navController.navigate(route = it)
                }
            )
        }
    }
}