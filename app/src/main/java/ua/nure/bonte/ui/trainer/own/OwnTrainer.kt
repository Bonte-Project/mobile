package ua.nure.bonte.ui.trainer.own

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.addmenu.AddMenu
import ua.nure.bonte.ui.compose.UserHolder
import java.time.LocalDate
import java.time.LocalTime

object OwnTrainer {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnCreateTrainerClick : Action
        data object OnDismissCreateTrainerDialog : Action
        data class OnSaveTrainer(
            val bio: String?,
            val certification: String?,
            val specialization: String?,
            val location: String? = null,
            val isActive: Boolean
        ) : OwnTrainer.Action
        data class OnDayClick(val date: LocalDate) : Action
        data object OnShowAddSessionDialog : Action
        data object OnDismissAddSessionDialog : Action
        data class OnSessionNameChanged(val name: String) : Action
        data object OnCreateSession : Action
        data object OnShowSelectTimeDialog : Action
        data object OnDismissSelectTimeDialog : Action
        data class OnSelectTime(val h: Int, val m: Int) : Action

        data object OnShowUserSelectDialog : Action
        data object OnUserSelectDialogDismiss : Action
        data class OnUserSelect(val user: ProfileEntity) : Action
        data class OnLoadUser(val userId: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val trainerId: String? = null,
        val trainer: Trainer? = null,
        val sessions: Map<Int, List<SessionEntity>>? = null,
        val showAddSessionDialog: Boolean = false,
        val selectedDay: LocalDate? = null,
        val selectedTime: LocalTime? = null,
        val sessionName: String? = null,
        val showSelectTimeDialog: Boolean = false,
        val showSelectUserDialog: Boolean = false,
        val selectedUser: ProfileEntity? = null,
        val users: List<UserHolder>? = null,
        val showCreateTrainerDialog: Boolean = false,
    )
}