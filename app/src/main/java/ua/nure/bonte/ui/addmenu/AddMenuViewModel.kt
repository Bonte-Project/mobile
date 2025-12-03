package ua.nure.bonte.ui.addmenu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.dto.ActivityLogRequest
import ua.nure.bonte.repository.dto.CreateSleepLogDto
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.sleeplog.SleepLogRepository
import ua.nure.bonte.repository.activity.ActivityRepository
import ua.nure.bonte.ui.addmenu.AddMenu.Event.*
import javax.inject.Inject

@HiltViewModel
class AddMenuViewModel @Inject constructor(
    private val sleepLogRepository: SleepLogRepository,
    private val activityLogRepository: ActivityRepository
) : ViewModel() {
    private val TAG by lazy { AddMenuViewModel::class.simpleName }

    private val _state = MutableStateFlow(AddMenu.State())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AddMenu.State()
    )

    private val _event = MutableSharedFlow<AddMenu.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: AddMenu.Action) = viewModelScope.launch {
        when (action) {
            AddMenu.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is AddMenu.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            AddMenu.Action.OnUpdateSleepLogClick -> {
                _state.update { it.copy(showSleepDialog = true) }
            }

            AddMenu.Action.OnUpdateActivityLogClick -> {
                _state.update { it.copy(showActivityDialog = true) }
            }

            AddMenu.Action.OnDismissSleepDialog -> {
                _state.update { it.copy(showSleepDialog = false) }
            }
            AddMenu.Action.OnDismissActivityDialog ->{
                _state.update { it.copy(showActivityDialog = false) }
            }

            is AddMenu.Action.OnSaveSleepLog -> {
                _state.update { it.copy(inProgress = true) }

                val createSleepLogDto = CreateSleepLogDto(
                    startTime = action.startTime,
                    endTime = action.endTime,
                    quality = action.quality
                )

                sleepLogRepository.createSleepLog(createSleepLogDto)
                    .onSuccess {
                        Log.d(TAG, "Sleep log saved successfully: $it")
                        _state.update { it.copy(showSleepDialog = false, inProgress = false) }
                        _event.emit(OnBack)
                    }.onError { error ->
                        Log.e(TAG, "Failed to save sleep log: $error")
                        _state.update { it.copy(inProgress = false) }
                    }
            }
            is AddMenu.Action.OnSaveActivityLog -> {
                _state.update { it.copy(inProgress = true) }

                val activityLogRequest = ActivityLogRequest(
                    activityType = action.activityType,
                    intensity = action.intensity,
                    durationMinutes = action.durationMinutes,
                    completedAt = action.completedAt,
                )

                activityLogRepository.createActivityLog(activityLogRequest)
                    .onSuccess {
                        Log.d(TAG, "Activity log saved successfully: $it")
                        _state.update { it.copy(showActivityDialog = false, inProgress = false) }
                        _event.emit(OnBack)
                    }.onError { error ->
                        Log.e(TAG, "Failed to save activity log: $error")
                        _state.update { it.copy(inProgress = false) }
                    }
            }
        }
    }
}