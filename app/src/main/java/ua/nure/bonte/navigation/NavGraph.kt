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
    }

}