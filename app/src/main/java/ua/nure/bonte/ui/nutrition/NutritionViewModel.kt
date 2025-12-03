package ua.nure.bonte.ui.nutrition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.nure.bonte.ui.nutrition.Nutrition.Action
import ua.nure.bonte.ui.nutrition.Nutrition.Event
import ua.nure.bonte.ui.nutrition.Nutrition.State
import javax.inject.Inject
import ua.nure.bonte.repository.dto.NutritionLogRequest
import ua.nure.bonte.repository.nutrition.NutritionRepository
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.dto.CreateSleepLogDto
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.ui.nutrition.Nutrition.Event.OnBack
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository
) : ViewModel() {
    private val TAG by lazy { NutritionViewModel::class.simpleName }
    private val _state = MutableStateFlow(State())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = State()
    )

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private fun loadGoals() = viewModelScope.launch {
        when (val result = nutritionRepository.getGoal()) {
            is Result.Success -> {
                _state.update { it.copy(goals = result.data) }
            }
            is Result.Error -> {
                _event.emit(Event.OnError("Помилка завантаження цілей"))
            }
        }
    }
    fun onAction(action: Action) = viewModelScope.launch {
        when (action) {
            Action.OnBack -> _event.emit(Event.OnBack)
            is Action.OnNavigate -> _event.emit(Event.OnNavigate(route = action.route))
            Nutrition.Action.OnUpdateNutritionLogClick -> {
                _state.update { it.copy(showNutritionDialog = true) }
            }
            Nutrition.Action.OnDismissNutritionDialog -> {
                _state.update { it.copy(showNutritionDialog = false) }
            }
            is Nutrition.Action.OnSaveNutritionLog -> {
                _state.update { it.copy(inProgress = true) }

                val nutritionLogRequest = NutritionLogRequest(
                    eatenAt = action.eatenAt,
                    mealType = action.mealType,
                    name = action.name,
                    calories = action.calories,
                    protein = action.protein,
                    carbs = action.carbs,
                    fat = action.fat,
                    weightInGrams = action.weightInGrams,

                )

                nutritionRepository.createLog(nutritionLogRequest)
                    .onSuccess {
                        Log.d(TAG, "Nutrition log saved successfully: $it")
                        _state.update { it.copy(showNutritionDialog = false, inProgress = false) }
                        _event.emit(OnBack)
                    }.onError { error ->
                        Log.e(TAG, "Failed to save nutrition log: $error")
                        _state.update { it.copy(inProgress = false) }
                    }
            }
        }
    }
}