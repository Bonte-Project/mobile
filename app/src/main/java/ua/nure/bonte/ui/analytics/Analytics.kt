package ua.nure.bonte.ui.analytics

import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ActivityLogDto
import ua.nure.bonte.repository.dto.NutritionLogDto
import ua.nure.bonte.repository.dto.SleepLogDto
import java.time.LocalDateTime

object Analytics {

    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object Refresh : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val nutritionLogs: List<NutritionLogDto> = emptyList(),
        val sleepLogs: List<SleepLogDto> = emptyList(),
        val activityLogs: List<ActivityLogDto> = emptyList(),

        val weekSleepData: List<Triple<Int, String, LocalDateTime>> = emptyList(),
        val weekActivityData: List<Triple<Int, String, LocalDateTime>> = emptyList(),
        val weekNutritionData: List<Triple<Int, String, LocalDateTime>> = emptyList(),

        val averageSleepTime: Double = 0.0,
        val sleepChangePercent: Int = 0,
        val averageActivityTime: Int = 0,
        val activityChangePercent: Int = 0,
        val averageCalories: Int = 0,
        val caloriesChangePercent: Int = 0
    )

}