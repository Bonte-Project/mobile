package ua.nure.bonte.ui.profile.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.activity.ActivityRepository
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.nutrition.NutritionRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sleeplog.SleepLogRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.profile.dashboard.Dashboard.Event.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val nutritionRepository: NutritionRepository,
    private val sleepRepository: SleepLogRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(Dashboard.State())
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), Dashboard.State()
    )

    private val _event = MutableSharedFlow<Dashboard.Event>()
    val event = _event.asSharedFlow()

    init {
        loadMe()
        observeMe()
        observeData()
        refreshAllLogs()
    }

    private fun refreshAllLogs() {
        viewModelScope.launch {
            nutritionRepository.refreshNutritionLogs()
            sleepRepository.refreshSleepLogs()
            activityRepository.refreshActivityLogs()
        }
    }

    fun onAction(action: Dashboard.Action) = viewModelScope.launch {
        when (action) {
            Dashboard.Action.OnBack -> _event.emit(OnBack)
            is Dashboard.Action.OnNavigate -> _event.emit(OnNavigate(action.route))
            Dashboard.Action.OnAddButtonClick -> _event.emit(OnNavigate(Screen.Profile.AddMenu))
            Dashboard.Action.Refresh -> refreshAllLogs()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            nutritionRepository.getLogs().collect { logs ->
                _state.update { it.copy(nutritionLogs = logs) }
            }
        }

        viewModelScope.launch {
            sleepRepository.getSleepLogs().collect { logs ->
                _state.update { it.copy(sleepLogs = logs) }
            }
        }

        viewModelScope.launch {
            activityRepository.getActivityLogs().collect { logs ->
                _state.update { it.copy(activityLogs = logs) }
            }
        }

        viewModelScope.launch {
            nutritionRepository.getGoal().onSuccess { goal ->
                _state.update { it.copy(goal = goal) }
            }
        }
    }

    private var loadMeJob: Job? = null

    private fun loadMe() {
        loadMeJob?.cancel()
        loadMeJob = viewModelScope.launch {
            userRepository.loadMe()
        }
    }

    private fun observeMe() = viewModelScope.launch {
        userRepository.getMe().collect { profile ->
            _state.update { it.copy(profile = profile.profileEntity) }
        }
    }
}
