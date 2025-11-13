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
        val avatarUrl: String? = if (BuildConfig.DEBUG) "https://placehold.co/64x64/FF9FC2/1A0D13?text=JS" else null,
        val name: String = if (BuildConfig.DEBUG) "John Smith" else "",
        val role: String = if (BuildConfig.DEBUG) "premium user" else "",
        val calories: String = if (BuildConfig.DEBUG) "2150" else "",
        val sleep: String = if (BuildConfig.DEBUG) "7h 30m" else "",
        val activity: String = if (BuildConfig.DEBUG) "10 steps" else "",
        val nutrition:  String = if (BuildConfig.DEBUG) "500 kcal" else "",
        val yoga:  String = if (BuildConfig.DEBUG) "60 min" else "",
        val sleepQuality:  String = if (BuildConfig.DEBUG) "Great" else "",
        val recommendations:  String = if (BuildConfig.DEBUG) "Based on your activity, we suggest a new workout routine. Also, if your goal is to lose weight we suggest...." else "",
    )
}