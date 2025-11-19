package ua.nure.bonte.ui.trainer

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.navigation.Screen

object Trainer {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnCreateTrainer : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val profile: Profile? = null,
    )
}