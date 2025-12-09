package ua.nure.bonte.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.bonte.ui.trainer.own.OwnTrainerScreen

fun NavGraphBuilder.ownTrainerGraph(navController: NavController) {
    navigation<NestedGraph.OwnTrainer>(
        startDestination = Screen.OwnTrainer.View
    ) {
        composable<Screen.OwnTrainer.View> {
            OwnTrainerScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}