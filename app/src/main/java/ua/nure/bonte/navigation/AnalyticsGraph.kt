package ua.nure.bonte.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.bonte.ui.analytics.AnalyticsScreen


fun NavGraphBuilder.analyticsGraph(navController: NavController) {
    navigation<NestedGraph.Analytics>(
        startDestination = Screen.Analytics.AnalyticsView
    ) {
        composable<Screen.Analytics.AnalyticsView> {
            AnalyticsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}
