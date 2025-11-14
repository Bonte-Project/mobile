package ua.nure.bonte.ui.profile.dashboard

import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.db.data.entity.ProfileEntity

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
        val profile: ProfileEntity? = null
    )
}