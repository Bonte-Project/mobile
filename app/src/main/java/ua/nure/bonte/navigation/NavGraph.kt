package ua.nure.bonte.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.nure.bonte.ui.auth.register.RegisterScreen
import ua.nure.bonte.ui.auth.signin.SignInScreen
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPasswordScreen
import ua.nure.bonte.ui.profile.dashboard.DashboardScreen
import ua.nure.bonte.ui.profile.settings.SettingsScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Auth.SignIn
    ) {
        composable<Screen.Auth.Registration> {
            RegisterScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Auth.SignIn> {
            SignInScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Auth.ForgotPassword> {
            ForgotPasswordScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
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