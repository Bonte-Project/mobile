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
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.Result
import javax.inject.Inject

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Trainer.State())
    val state = _state.onStart {
        observerProfile()
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
        when (action) {
            Trainer.Action.OnBack -> _event.emit(Trainer.Event.OnBack)
            is Trainer.Action.OnNavigate -> _event.emit(Trainer.Event.OnNavigate(route = action.route))
            Trainer.Action.OnCreateTrainer -> createTrainer()
            Trainer.Action.LoadTrainer -> loadTrainer()
        }
    }

    private fun observerProfile() = viewModelScope.launch {
        userRepository.getMe().collect { profile ->
            _state.update { s ->
                s.copy(profile = profile)
            }
        }
        loadTrainer()
    }

    fun loadTrainer() = viewModelScope.launch {
        when (val result = trainerRepository.loadTrainer()) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка завантаження тренера: ${result.error}")
            )
        }
    }

    private fun createTrainer() = viewModelScope.launch {
        val request = TrainerRequest(
            bio = "",
            certification = "",
            specialization = "",
            location = "",
            isActive = true
        )
        when (val result = trainerRepository.createTrainer(request)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка створення тренера: ${result.error}")
            )
        }
    }

    fun updateTrainer(request: TrainerRequest) = viewModelScope.launch {
        when (val result = trainerRepository.updateTrainer(request)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка оновлення тренера: ${result.error}")
            )
        }
    }

    fun getTrainerById(id: String) = viewModelScope.launch {
        when (val result = trainerRepository.getTrainerById(id)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка отримання тренера: ${result.error}")
            )
        }
    }

    fun addExperience(request: ExperienceRequest) = viewModelScope.launch {
        when (val result = trainerRepository.addExperience(request)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка додавання досвіду: ${result.error}")
            )
        }
    }

    fun updateExperience(experienceId: String, request: ExperienceRequest) = viewModelScope.launch {
        when (val result = trainerRepository.updateExperience(experienceId, request)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка оновлення досвіду: ${result.error}")
            )
        }
    }

    fun deleteExperience(experienceId: String) = viewModelScope.launch {
        when (val result = trainerRepository.deleteExperience(experienceId)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error -> _event.emit(
                Trainer.Event.OnError("Помилка видалення досвіду: ${result.error}")
            )
        }
    }

    fun observeTrainerFlow() = viewModelScope.launch {
        trainerRepository.getTrainer().collect { trainerResponse ->
            _trainerState.value = trainerResponse
        }
    }
}
