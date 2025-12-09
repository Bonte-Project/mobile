package ua.nure.bonte.ui.trainer.own

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.ui.trainer.view.Trainer

import javax.inject.Inject

@HiltViewModel
class OwnTrainerViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(OwnTrainer.State())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OwnTrainer.State()
    )
    private val _event = MutableSharedFlow<OwnTrainer.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: OwnTrainer.Action) = viewModelScope.launch {
        when(action) {
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
            is OwnTrainer.Action.OnNavigate -> _event.emit(OwnTrainer.Event.OnNavigate(route = action.route))
            OwnTrainer.Action.OnShowAddSessionDialog -> {
                _state.update { s ->
                    s.copy(
                        showAddSessionDialog = true
                    )
                }
            }
        }
    }
}