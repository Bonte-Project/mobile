package ua.nure.bonte.ui.trainer.own

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.dto.CreateSleepLogDto
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.messages.MessagesRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sessions.SessionsRepository
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.addmenu.AddMenu
import ua.nure.bonte.ui.compose.UserHolder
import ua.nure.bonte.ui.trainer.own.OwnTrainer.Event.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import javax.inject.Inject

@HiltViewModel
class OwnTrainerViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val sessionsRepository: SessionsRepository,
    private val messagesRepository: MessagesRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(OwnTrainer.State())
    val state = _state.onStart {
        loadOwnTrainer()
        observeTrainer()
        loadSessions()
        getChatUsers()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OwnTrainer.State()
    )
    private val _event = MutableSharedFlow<OwnTrainer.Event>()
    val event = _event.asSharedFlow()

    private var loadTrainerJob: Job? = null
    private var createSessionJob: Job? = null
    private var loadUsersListJob: Job? = null

    private val loadedUsersIds = mutableListOf<String>()

    fun onAction(action: OwnTrainer.Action) = viewModelScope.launch {
        when (action) {
            OwnTrainer.Action.OnBack -> _event.emit(OnBack)
            OwnTrainer.Action.OnCreateTrainerClick -> {
                _state.update { it.copy(showCreateTrainerDialog = true) }
            }

            OwnTrainer.Action.OnDismissCreateTrainerDialog -> {
                _state.update { it.copy(showCreateTrainerDialog = false) }
            }
            is OwnTrainer.Action.OnSaveTrainer -> {
                _state.update { it.copy(inProgress = true) }

                val trainerRequest = TrainerRequest(
                    bio = action.bio,
                    certification = action.certification,
                    specialization = action.specialization,
                    location = action.location,
                    isActive = action.isActive,
                )

                trainerRepository.createTrainer(trainerRequest)
                    .onSuccess {
                        Log.d(TAG, "Trainer saved successfully: $it")
                        _state.update { it.copy(showCreateTrainerDialog = false, inProgress = false) }
                        _event.emit(OwnTrainer.Event.OnBack)
                    }.onError { error ->
                        Log.e(TAG, "Failed to save trainer: $error")
                        _state.update { it.copy(inProgress = false) }
                    }
            }
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
                createSession(
                    name = state.value.sessionName ?: "",
                    userId = state.value.selectedUser?.id ?: "",
                    scheduledAt = state.value.selectedDay
                        ?.atStartOfDay()
                        ?.withHour(state.value.selectedTime?.hour ?: 0)
                        ?.withMinute(state.value.selectedTime?.minute ?: 0)
                        ?: LocalDateTime.now()
                )
            }
            is OwnTrainer.Action.OnSessionNameChanged -> {
                _state.update { s ->
                    s.copy(
                        sessionName = action.name
                    )
                }
            }

            OwnTrainer.Action.OnDismissSelectTimeDialog -> {
                _state.update { s ->
                    s.copy(
                        showSelectTimeDialog = false,
                        showAddSessionDialog = true,
                    )
                }
            }
            OwnTrainer.Action.OnShowSelectTimeDialog -> {
                _state.update { s ->
                    s.copy(
                        showSelectTimeDialog = true,
                        showAddSessionDialog = false,
                    )
                }
            }
            is OwnTrainer.Action.OnSelectTime -> {
                _state.update { s ->
                    s.copy(
                        selectedTime = LocalTime.of(action.h, action.m),
                        showSelectTimeDialog = false,
                        showAddSessionDialog = true
                    )
                }
            }

            OwnTrainer.Action.OnShowUserSelectDialog -> {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = false,
                        showSelectUserDialog = true

                    )
                }
            }
            is OwnTrainer.Action.OnUserSelect -> {
                _state.update { s ->
                    s.copy(
                        selectedUser = action.user,
                        showSelectUserDialog = false,
                        showAddSessionDialog = true,
                    )
                }
            }
            OwnTrainer.Action.OnUserSelectDialogDismiss -> {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = true,
                        showSelectUserDialog = false
                    )
                }
            }

            is OwnTrainer.Action.OnLoadUser -> {
                loadUserById(userId = action.userId)
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
                        sessions = trainer.sessions?.groupBy { it.scheduledAt.dayOfYear }?.map { (key, value) ->
                            key to value.sortedByDescending { it.scheduledAt }
                        }?.toMap()
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

    private fun createSession(name: String, userId: String, scheduledAt: LocalDateTime) {
        createSessionJob?.cancel()
        createSessionJob = viewModelScope.launch {
            sessionsRepository.createSession(
                name = name,
                userId = userId,
                scheduledAt = scheduledAt
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = false,
                        selectedDay = null,
                        selectedTime = null,
                        selectedUser = null,
                        sessionName = null
                    )
                }
            }
        }

    }

    private fun getChatUsers() {
        loadUsersListJob?.cancel()
        loadUsersListJob = viewModelScope.launch {
            messagesRepository.loadChatList(
            ).onSuccess { idsData ->
                userRepository
                    .getUsersFromList(ids = idsData.data)
                    .collect { users ->
                        val userMap = users.associate { it.id to it }
                        _state.update { s ->
                            s.copy(
                                users = idsData.data.map {
                                    UserHolder(
                                        userId = it,
                                        user = userMap[it]
                                    )
                                }
                            )
                        }
                    }
            }
        }
    }

    private fun loadUserById(userId: String) = viewModelScope.launch {
        if(userId in loadedUsersIds) {
            return@launch
        }
        userRepository.getUserById(
            id = userId
        ).onSuccess {
            loadedUsersIds.add(userId)
        }
    }

}