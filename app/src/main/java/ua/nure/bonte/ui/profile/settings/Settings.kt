package ua.nure.bonte.ui.profile.settings

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.auth.register.Register

object Settings {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnAgeChange(val age: String) : Settings.Action
        data class OnWeightChange(val weight: String) : Settings.Action
        data class OnHeightChange(val height: String) : Settings.Action
    }

    data class State(
        val inProgress: Boolean = false,
        val age: String = if (BuildConfig.DEBUG) "18" else "",
        val weight: String = if (BuildConfig.DEBUG) "60" else "",
        val height: String = if (BuildConfig.DEBUG) "180" else "",
        )
}