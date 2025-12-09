package ua.nure.bonte.ui.trainer.own

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sessions.SessionsRepository
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.ui.trainer.own.OwnTrainer.Event.*
import ua.nure.bonte.ui.trainer.view.Trainer

import javax.inject.Inject

@HiltViewModel
class OwnTrainerViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val sessionsRepository: SessionsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(OwnTrainer.State())
    val state = _state.onStart {
        loadOwnTrainer()
        observeTrainer()
        loadSessions()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OwnTrainer.State()
    )
    private val _event = MutableSharedFlow<OwnTrainer.Event>()
    val event = _event.asSharedFlow()

    private var loadTrainerJob: Job? = null
    private var createSessionJob: Job? = null

    fun onAction(action: OwnTrainer.Action) = viewModelScope.launch {
        when (action) {
            OwnTrainer.Action.OnBack -> _event.emit(OwnTrainer.Event.OnBack)
            OwnTrainer.Action.OnCreateTrainer -> {}
            is OwnTrainer.Action.OnDayClick -> {
                _state.update { s ->
                    s.copy(
                        selectedDay = action.date,
                        showAddSessionDialog = true
                    )
                }
            }

            OwnTrainer.Action.OnDismissAddSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        selectedDay = null,
                        showAddSessionDialog = false
                    )
                }
            }

            is OwnTrainer.Action.OnNavigate -> _event.emit(OnNavigate(route = action.route))
            OwnTrainer.Action.OnShowAddSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = true
                    )
                }
            }

            OwnTrainer.Action.OnCreateSession -> {
                createSession()
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = false,
                        selectedDay = null,
                    )
                }

            }
            is OwnTrainer.Action.OnSessionNameChanged -> {
                _state.update { s ->
                    s.copy(
                        sessionName = action.name
                    )
                }
            }
        }
    }

    private fun loadOwnTrainer() {
        if (_state.value.trainer != null) {
            return
        }
        loadTrainerJob?.cancel()
        loadTrainerJob = viewModelScope.launch {
            trainerRepository.loadMyTrainer()
                .onSuccess { data ->
                    _state.update { s ->
                        s.copy(
                            inProgress = false,
                            trainerId = data.trainer.id
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTrainer() = viewModelScope.launch {
        _state
            .map { it.trainerId }
            .distinctUntilChanged()
            .filterNotNull()
            .flatMapLatest { trainerId ->
                trainerRepository.getTrainerFlowById(id = trainerId)
            }.collect { trainer ->
                _state.update { s ->
                    s.copy(
                        trainer = trainer,
                        sessions = trainer.sessions?.groupBy { it.scheduledAt.dayOfYear }
                    )
                }
            }
    }

    private fun loadSessions() = viewModelScope.launch {
        _state
            .map { it.trainerId }
            .distinctUntilChanged()
            .filterNotNull()
            .collect {
                sessionsRepository.getTrainerSessions()
            }
    }

    private fun createSession() {
        createSessionJob?.cancel()
        createSessionJob = viewModelScope.launch {

        }

    }


}