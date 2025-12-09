package ua.nure.bonte.ui.trainer.view

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.profile.dashboard.Dashboard
import java.time.LocalDate

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
        data class OnDayClick(val date: LocalDate) : Action
        data object OnShowAddSessionDialog : Action
        data object OnDismissAddSessionDialog : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val trainerId: String? = null,
        val trainer: Trainer? = null,
        val sessions: Map<Int, List<SessionEntity>>? = null,
        val showAddSessionDialog: Boolean = false,
        val selectedDay: LocalDate? = null,
    )
}
