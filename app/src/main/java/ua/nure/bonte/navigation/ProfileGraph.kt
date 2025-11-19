package ua.nure.bonte.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.bonte.ui.profile.dashboard.DashboardScreen
import ua.nure.bonte.ui.profile.settings.SettingsScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<NestedGraph.Dashboard>(
        startDestination = Screen.Profile.Dashboard
    ) {
        composable<Screen.Profile.Dashboard> {
            DashboardScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Profile.Settings> {
            SettingsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}