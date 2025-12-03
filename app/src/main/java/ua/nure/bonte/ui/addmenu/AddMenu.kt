package ua.nure.bonte.ui.addmenu

import ua.nure.bonte.navigation.Screen

object AddMenu {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnAddFoodClick : Action
        data object OnUpdateSleepLogClick : Action
        data object OnUpdateActivityLogClick : Action
        data object OnDismissSleepDialog : Action
        data class OnSaveSleepLog(
            val startTime: String,
            val endTime: String,
            val quality: Int
        ) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val showSleepDialog: Boolean = false
    )
}