package ua.nure.bonte.ui.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.Result
import ua.nure.bonte.ui.trainer.Trainer.Event.*
import javax.inject.Inject

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Trainer.State())
    val state = _state.onStart {
        observerTrainer()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = Trainer.State()
    )

    private val _event = MutableSharedFlow<Trainer.Event>()
    val event = _event.asSharedFlow()

    private val _trainerState = MutableStateFlow<TrainerResponse?>(null)
    val trainerState = _trainerState

    fun onAction(action: Trainer.Action) = viewModelScope.launch {
        when(action) {
            Trainer.Action.OnBack -> _event.emit(Trainer.Event.OnBack)
            is Trainer.Action.OnNavigate -> _event.emit(Trainer.Event.OnNavigate(route = action.route))
            Trainer.Action.OnCreateTrainer -> {
            }
            Trainer.Action.LoadTrainer -> loadTrainer()
        }
    }

    private fun observerTrainer() = viewModelScope.launch {
        userRepository.getMe().collect { profile ->
            _state.update { s ->
                s.copy(profile = profile)
            }
        }

        loadTrainer()
    }

    private fun loadTrainer() = viewModelScope.launch {
        when(val result = trainerRepository.loadTrainer()) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка завантаження тренера: ${result.error}")
            )
        }
    }
}
