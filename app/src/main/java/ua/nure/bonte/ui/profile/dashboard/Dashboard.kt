package ua.nure.bonte.ui.profile.dashboard

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen

object Dashboard {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val avatarUrl: String? = null,
        val name: String? = null,
        val role: String? = null,
    )
}