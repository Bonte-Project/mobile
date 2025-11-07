package ua.nure.bonte.ui.auth.signin

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
    }

    data class State(
        val inProgress: Boolean = false,
    )
}