package ua.nure.bonte.ui.trainer.editsessions

import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.UserHolder
import java.time.LocalDate
import java.time.LocalTime

object EditSessions {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnShowConfirmDeleteDialog(val sessionId: String) : Action
        data object OnDismissConfirmDeleteDialog : Action
        data class OnShowEditSessionDialog(val sessionId: String) : Action
        data object OnDismissEditSessionDialog : Action
        data object OnDeleteSession : Action
        data class OnSessionNameChanged(val name: String) : Action
        data object OnEditSessionConfirmed : Action
        data object OnShowSelectTimeDialog : Action
        data object OnDismissSelectTimeDialog : Action
        data class OnTimeSelect(val hour: Int, val min: Int) : Action
        data class OnDateSelect(val date: LocalDate? = null) : Action

        data object OnShowSelectDateDialog : Action
        data object OnDismissSelectDateDialog : Action
        data class OnStatusChange(val session: SessionEntity) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val trainerId: String? = null,
        val trainer: Trainer? = null,
        val sessions: List<SessionEntity>? = null,
        val selectedDay: LocalDate? = null,
        val sessionName: String? = null,
        val showSelectTimeDialog: Boolean = false,
        val showSelectUserDialog: Boolean = false,
        val selectedUser: ProfileEntity? = null,
        val users: List<UserHolder>? = null,
        val day: LocalDate? = null,
        val showConfirmDeleteDialog: Boolean = false,
        val showEditSessionDialog: Boolean = false,
        val sessionId: String? = null,
        val editedSession: SessionEntity? = null,
        val showTimeSelectDialog: Boolean = false,
        val showDateSelectDialog: Boolean = false,
    )
}