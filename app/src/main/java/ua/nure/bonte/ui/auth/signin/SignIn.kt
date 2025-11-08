package ua.nure.bonte.ui.auth.signin

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen

object SignIn {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnGoogleSignIn(val idToken: String, val email: String) : Action
        data object OnSignIn : Action
        data class OnEmailChange(val email: String) : Action
        data class OnPasswordChange(val password: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val email: String = if (BuildConfig.DEBUG) "john.dow@gmail.com" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret1" else "",
    )
}