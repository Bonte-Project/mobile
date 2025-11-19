package ua.nure.bonte.navigation

import androidx.compose.material3.Text
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.bonte.ui.trainer.TrainerScreen

fun NavGraphBuilder.trainerGraph(navController: NavController) {
    navigation<NestedGraph.Trainer>(
        startDestination = Screen.Trainer.TrainerProfile
    ) {
        composable<Screen.Trainer.TrainerProfile> {
            TrainerScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}