package ua.nure.bonte.ui.trainer.view

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.navigation.Screen

object Trainer {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
        data class OnError(val message: String) : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnCreateTrainer : Action
        object LoadTrainer : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val profile: Profile? = null,
    )
}
