package ua.nure.bonte.ui.trainer.list

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ExperienceRequest

object TrainerList {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnLoadProfile(val profileId: String) : Action

    }

    data class State(
        val inProgress: Boolean = false,
        val trainers: List<Trainer>? = null,
    )
}