package ua.nure.bonte.ui.trainer.view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import ua.nure.bonte.db.data.entity.Trainer as TrainerDb
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sessions.SessionsRepository
import ua.nure.bonte.ui.chats.chatlist.ChatList
import ua.nure.bonte.ui.trainer.view.Trainer.Event.*

import javax.inject.Inject

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val sessionsRepository: SessionsRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val params = savedStateHandle.toRoute<Screen.Trainer.TrainerProfile>()
    private val _state = MutableStateFlow(Trainer.State(
        trainerId = params.trainerId
    ))
    val state: StateFlow<Trainer.State> = _state.asStateFlow()

    private val _event = MutableSharedFlow<Trainer.Event>()
    val event = _event.asSharedFlow()

    private var loadSessionsJob: Job? = null
    private var loadTrainerJob: Job? = null

    init {
        loadOwnTrainer()
        observeMyTrainer()
        observeTrainer()
        loadSessions(trainerId = params.trainerId)
    }

    fun onAction(action: Trainer.Action) = viewModelScope.launch {
        when (action) {
            Trainer.Action.OnBack -> _event.emit(Trainer.Event.OnBack)
            is Trainer.Action.OnNavigate -> _event.emit(OnNavigate(action.route))
            Trainer.Action.LoadTrainer -> Unit//loadTrainer()
            Trainer.Action.OnCreateTrainer -> {}
            is Trainer.Action.OnDayClick -> {
                _state.update { s ->
                    s.copy(
                        selectedDay = action.date,
                        showAddSessionDialog = true
                    )
                }
            }
            Trainer.Action.OnDismissAddSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        selectedDay = null,
                        showAddSessionDialog = false
                    )
                }
            }
            Trainer.Action.OnShowAddSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = true
                    )
                }
            }
            is Trainer.Action.OnChatClick -> {
                if (action.chatId != null) {
                    _event.emit(Trainer.Event.OnNavigate(Screen.Chat.TrainerChat(action.chatId)))
                }
            }
        }
    }
    private fun observeTrainer() = viewModelScope.launch {
        trainerRepository.getTrainerFlowById(id = params.trainerId).collect { trainer ->
            _state.update { s ->
                s.copy(
                    trainer = trainer,
                    sessions = trainer.sessions?.groupBy { it.scheduledAt.dayOfYear }
                )
            }
        }
    }

    private fun loadSessions(trainerId: String) {
        loadSessionsJob?.cancel()
        loadSessionsJob = viewModelScope.launch {
            sessionsRepository.getTrainerSessionsByTrainerId(id = trainerId)
        }
    }

    private fun loadOwnTrainer() {
        if (_state.value.myTrainer != null) {
            return
        }
        loadTrainerJob?.cancel()
        loadTrainerJob = viewModelScope.launch {
            trainerRepository.loadMyTrainer()
                .onSuccess { data ->
                    _state.update { s ->
                        s.copy(
                            inProgress = false,
                            myTrainerId = data.trainer.id
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeMyTrainer() = viewModelScope.launch {
        _state
            .map { it.myTrainerId }
            .distinctUntilChanged()
            .filterNotNull()
            .flatMapLatest { myTrainerId ->
                trainerRepository.getTrainerFlowById(id = myTrainerId)
            }.collect { trainer ->
                _state.update { s ->
                    s.copy(
                        myTrainer = trainer,
                    )
                }
            }
    }
}