package ua.nure.bonte.ui.nutrition

import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.NutritionLogDto
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.ui.addmenu.AddMenu

object Nutrition {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
        data class OnError(val message: String) : Event
        data class ShowSuccess(val message: String) : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnUpdateNutritionLogClick : Nutrition.Action
        data object OnDismissNutritionDialog : Nutrition.Action
        data class OnSaveNutritionLog(
            val eatenAt: String,
            val mealType: String,
            val name: String,
            val calories: Int,
            val protein: Int,
            val carbs: Int,
            val fat: Int,
            val weightInGrams: Int
        ) : Nutrition.Action
    }

    data class State(
        val inProgress: Boolean = false,
        val showNutritionDialog: Boolean = false,
        val logs: List<NutritionLogDto> = emptyList(),
        val goals: NutritionGoalRequest? = null
    )
}