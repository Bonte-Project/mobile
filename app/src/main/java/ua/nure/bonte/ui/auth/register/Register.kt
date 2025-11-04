package ua.nure.bonte.ui.auth.register

import ua.nure.bonte.navigation.Screen

object Register {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnRegister : Action
    }

    data class State(
        val inProgress: Boolean = false,
    )
}