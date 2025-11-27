package ua.nure.bonte.ui.trainer

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ExperienceRequest

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

        data class OnBioChange(val bio: String) : Action
        data class OnCertificationChange(val certification: String) : Action
        data class OnSpecializationChange(val specialization: String) : Action
        data class OnLocationChange(val location: String) : Action
        data object OnSaveProfile : Action
        data object OnDeleteProfile : Action

        data object OnAddExperience : Action
        data class OnDeleteExperience(val experienceId: String) : Action
        data class OnExperienceChange(
            val experienceId: String,
            val title: String,
            val description: String,
            val startDate: String,
            val endDate: String
        ) : Action
        data class OnAddExperienceWithData(val request: ExperienceRequest) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val profile: Profile? = null,
    )
}