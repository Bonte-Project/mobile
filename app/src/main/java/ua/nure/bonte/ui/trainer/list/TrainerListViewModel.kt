package ua.nure.bonte.ui.trainer.list

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
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.trainer.list.TrainerList.Event.*
import javax.inject.Inject

@HiltViewModel
class TrainerListViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TrainerList.State())
    val state = _state.onStart {
        loadTrainers()
        observeTrainers()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = TrainerList.State()
    )

    private val _event = MutableSharedFlow<TrainerList.Event>()
    val event = _event.asSharedFlow()

    private val loadedProfilesIds = mutableListOf<String>()

    fun onAction(action: TrainerList.Action) = viewModelScope.launch {
        when(action) {
            TrainerList.Action.OnBack -> {
                _event.emit(TrainerList.Event.OnBack)
            }
            is TrainerList.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            is TrainerList.Action.OnLoadProfile -> {
                loadProfileById(profileId = action.profileId)
            }
        }
    }

    private fun loadTrainers() = viewModelScope.launch {
        if (_state.value.trainers != null) {
            return@launch
        }
        trainerRepository.loadTrainers()

    }

    private fun observeTrainers() = viewModelScope.launch {
        trainerRepository.getTrainers().collect { list ->
            _state.update { s ->
                s.copy(
                    trainers = list
                )
            }

        }
    }

    private fun loadProfileById(profileId: String) = viewModelScope.launch {
        if(profileId in loadedProfilesIds) {
            return@launch
        }
        userRepository.getUserById(
            id = profileId
        ).onSuccess {
            loadedProfilesIds.add(profileId)
        }
    }
}