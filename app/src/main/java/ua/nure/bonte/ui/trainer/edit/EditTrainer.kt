package ua.nure.bonte.ui.trainer.edit

import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.navigation.Screen

object EditTrainer {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action

        data class OnBioChange(val bio: String?) : Action
        data class OnCertificationChange(val certification: String?) : Action
        data class OnSpecializationChange(val specialization: String?) : Action
        data class OnLocationChange(val location: String?) : Action

        data object OnDismissAddExperienceDialog : Action
        data object OnAddExperienceClick : Action
        data class OnSaveExperience(
            val title: String,
            val description: String?,
            val startDate: String?,
            val endDate: String?,
        ) : Action

        data class OnExperienceTitleChange(val experienceId: String, val title: String) : Action
        data class OnExperienceDescriptionChange(val experienceId: String, val description: String?) : Action
        data class OnExperienceDelete(val experienceId: String) : Action
    }

    data class State(
        val trainer: TrainerEntity? = null,
        val experiences: List<ExperienceEntity> = emptyList(),
        val inProgress: Boolean = false,
        val showAddExperienceDialog: Boolean = false,
    )
}
