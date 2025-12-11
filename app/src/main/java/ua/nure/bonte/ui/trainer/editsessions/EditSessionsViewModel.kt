package ua.nure.bonte.ui.trainer.editsessions

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sessions.SessionsRepository
import ua.nure.bonte.ui.trainer.own.OwnTrainer.Event.OnBack
import ua.nure.bonte.ui.trainer.own.OwnTrainer.Event.OnNavigate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EditSessionsViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG by lazy { EditSessionsViewModel::class.simpleName }

    val params = savedStateHandle.toRoute<Screen.OwnTrainer.EditSessions>()
    val day = LocalDate.ofEpochDay(params.day)
    private val _state = MutableStateFlow(
        EditSessions.State(
            day = day
        )
    )
    val state = _state.onStart {
        observeSessions(
            trainerId = params.trainerId,
            day = day
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EditSessions.State(day = day)
    )
    private val _event = MutableSharedFlow<EditSessions.Event>()
    val event = _event.asSharedFlow()

    private var updateSessionJob: Job? = null


    fun onAction(action: EditSessions.Action) = viewModelScope.launch {
        when (action) {
            EditSessions.Action.OnBack -> _event.emit(EditSessions.Event.OnBack)
            is EditSessions.Action.OnNavigate -> _event.emit(EditSessions.Event.OnNavigate(route = action.route))
            EditSessions.Action.OnDeleteSession -> {
                state.value.sessionId?.let { id ->
                    deleteSession(sessionId = id)
                }
            }

            EditSessions.Action.OnDismissConfirmDeleteDialog -> {
                _state.update { s ->
                    s.copy(
                        sessionId = null,
                        showConfirmDeleteDialog = false
                    )
                }

            }

            EditSessions.Action.OnDismissEditSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        editedSession = null,
                        showConfirmDeleteDialog = false
                    )
                }
            }

            is EditSessions.Action.OnShowConfirmDeleteDialog -> {
                _state.update { s ->
                    s.copy(
                        sessionId = action.sessionId,
                        showConfirmDeleteDialog = true
                    )
                }
            }

            is EditSessions.Action.OnShowEditSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        editedSession = s.sessions?.first { it.id == action.sessionId },
                        showEditSessionDialog = true
                    )
                }
            }

            EditSessions.Action.OnDismissSelectTimeDialog -> {
                _state.update { s ->
                    s.copy(
                        showTimeSelectDialog = false,
                        showEditSessionDialog = true,
                    )
                }

            }

            EditSessions.Action.OnEditSessionConfirmed -> {
                updateSession(
                    sessionId = state.value.editedSession?.id ?: "",
                    name = state.value.editedSession?.name,
                    scheduledAt = state.value.editedSession?.scheduledAt,
                    state.value.editedSession?.status ?: SessionStatus.scheduled
                )
            }

            is EditSessions.Action.OnSessionNameChanged -> {
                _state.update { s ->
                    s.copy(
                        editedSession = s.editedSession?.copy(
                            name = action.name
                        )
                    )
                }
            }

            EditSessions.Action.OnShowSelectTimeDialog -> {
                _state.update { s ->
                    s.copy(
                        showTimeSelectDialog = true,
                        showEditSessionDialog = false
                    )
                }
            }

            is EditSessions.Action.OnTimeSelect -> {
                _state.update { s ->
                    s.copy(
                        editedSession = s.editedSession?.copy(
                            scheduledAt = state.value.editedSession
                                ?.scheduledAt
                                ?.withHour(action.hour)
                                ?.withMinute(action.min)
                                ?: LocalDateTime.now()
                        ),
                        showSelectTimeDialog = false,
                        showEditSessionDialog = true
                    )
                }
            }

            is EditSessions.Action.OnDateSelect -> {
                action.date?.dayOfYear?.let { day ->
                    _state.update { s ->

                        s.copy(
                            editedSession = s.editedSession?.copy(
                                scheduledAt = state.value.editedSession
                                    ?.scheduledAt
                                    ?.withDayOfYear(day)
                                    ?: LocalDateTime.now()
                            ),
                            showDateSelectDialog = false,
                            showEditSessionDialog = true
                        )

                    }
                }
            }

            EditSessions.Action.OnDismissSelectDateDialog -> {
                _state.update { s ->
                    s.copy(
                        showDateSelectDialog = false,
                        showEditSessionDialog = true
                    )
                }
            }

            EditSessions.Action.OnShowSelectDateDialog -> {
                _state.update { s ->
                    s.copy(
                        showDateSelectDialog = true,
                        showEditSessionDialog = false
                    )
                }
            }

            is EditSessions.Action.OnStatusChange -> {
                updateSession(
                    sessionId = action.session.id,
                    name = action.session.name,
                    scheduledAt = action.session.scheduledAt,
                    status = action.session.status
                )
            }
        }
    }

    private fun observeSessions(trainerId: String, day: LocalDate) = viewModelScope.launch {
        sessionsRepository.getSessionsByDay(
            trainerId = trainerId,
            day = day
        ).collect { list ->
            _state.update { s ->
                s.copy(
                    sessions = list.sortedBy { it.scheduledAt }
                )
            }
        }
    }

    private fun deleteSession(sessionId: String) = viewModelScope.launch {
        sessionsRepository.deleteSession(
            id = sessionId
        ).onSuccess {
            _state.update { s ->
                s.copy(
                    showConfirmDeleteDialog = false,
                    sessionId = null
                )
            }
        }
    }

    private fun updateSession(sessionId: String, name: String? = null ,scheduledAt: LocalDateTime? = null, status: SessionStatus) {
        updateSessionJob?.cancel()
        updateSessionJob = viewModelScope.launch {
            sessionsRepository.updateSession(
                id = sessionId,
                name = name ?: "",
                scheduledAt = scheduledAt ?: LocalDateTime.now(),
                status =  status
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showEditSessionDialog = false,
                        editedSession = null,
                    )
                }
            }
        }
    }
}