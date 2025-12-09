package ua.nure.bonte.ui.profile.dashboard

import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.dto.ActivityLogDto
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.repository.dto.NutritionLogDto
import ua.nure.bonte.repository.dto.SleepLogDto
import java.time.LocalDate

object Dashboard {

    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnAddButtonClick : Action
        data object Refresh : Action
        data class OnDayClick(val date: LocalDate) : Action
        data object OnShowAddSessionDialog : Action
        data object OnDismissAddSessionDialog : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val profile: Profile? = null,
        val sessions: Map<Int, List<SessionEntity>>? = null,
        val nutritionLogs: List<NutritionLogDto> = emptyList(),
        val sleepLogs: List<SleepLogDto> = emptyList(),
        val activityLogs: List<ActivityLogDto> = emptyList(),
        val goal: NutritionGoalRequest? = null,
        val showAddSessionDialog: Boolean = false,
        val selectedDay: LocalDate? = null,
    )
}
