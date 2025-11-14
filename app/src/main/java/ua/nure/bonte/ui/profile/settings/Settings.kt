package ua.nure.bonte.ui.profile.settings

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.ui.auth.register.Register

object Settings {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnAgeChange(val age: Int?) : Settings.Action
        data class OnWeightChange(val weight: Int?) : Settings.Action
        data class OnHeightChange(val height: Int?) : Settings.Action
        data class OnFirstNameChange(val firstName: String?) : Settings.Action
        data class OnLastNameChange(val lastName: String?) : Settings.Action
        data class OnAvatarChange(val avatarUrl: String?) : Settings.Action
        data class OnEmailChange(val email: String?) : Action

    }

    data class State(
        val profile: ProfileEntity? = null,
        val inProgress: Boolean = false,
    )
}