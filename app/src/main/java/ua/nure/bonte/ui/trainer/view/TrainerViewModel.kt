package ua.nure.bonte.ui.trainer.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.nure.bonte.db.data.entity.Trainer as TrainerDb
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.dto.TrainerResponse

import javax.inject.Inject

@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Trainer.State())
    val state: StateFlow<Trainer.State> = _state.asStateFlow()

    private val _event = MutableSharedFlow<Trainer.Event>()
    val event = _event.asSharedFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() = viewModelScope.launch {
        userRepository.getMe()
            .distinctUntilChanged()
            .collectLatest { profile ->
                _state.update { it.copy(profile = profile) }
                loadTrainer()
            }
    }
    fun refreshTrainer() {
        loadTrainer()
    }

    fun onAction(action: Trainer.Action) = viewModelScope.launch {
        when (action) {
            Trainer.Action.OnBack -> _event.emit(Trainer.Event.OnBack)
            is Trainer.Action.OnNavigate -> _event.emit(Trainer.Event.OnNavigate(action.route))
            Trainer.Action.LoadTrainer -> loadTrainer()
            Trainer.Action.OnCreateTrainer -> {}
        }
    }

    fun loadTrainer() = viewModelScope.launch {
        val profile = _state.value.profile ?: return@launch
        when (val result = trainerRepository.loadMyTrainer()) {
            is Result.Success -> {
                val trainerResponse = result.data
                val trainerEntity = TrainerDb(
                    trainerEntity = TrainerEntity(
                        trainerId = trainerResponse.trainer.id,
                        userId = profile.profileEntity.id,
                        bio = trainerResponse.trainer.bio,
                        certification = trainerResponse.trainer.certification,
                        specialization = trainerResponse.trainer.specialization,
                        location = trainerResponse.trainer.location,
                        isActive = trainerResponse.trainer.isActive
                    ),
                    experience = trainerResponse.trainer.experience?.map { exp ->
                        ua.nure.bonte.db.data.entity.ExperienceEntity(
                            experienceId = exp.id,
                            trainerId = trainerResponse.trainer.id,
                            title = exp.title,
                            description = exp.description,
                            startDate = exp.startDate,
                            endDate = exp.endDate
                        )
                    } ?: emptyList(),
                    profile = profile.profileEntity
                )

                _state.update { s ->
                    s.copy(profile = profile.copy(trainer = trainerEntity))
                }
            }
            is Result.Error -> {
                if (result.error !is DataError.Remote) {
                    _event.emit(Trainer.Event.OnError("Помилка завантаження тренера: ${result.error}"))
                }
            }
        }
    }

    fun createTrainer(
        bio: String,
        certification: String,
        specialization: String,
        experience: List<ExperienceRequest>,
        location: String
    ) = viewModelScope.launch {
        val req = ua.nure.bonte.repository.dto.TrainerRequest(
            bio = bio,
            certification = certification,
            specialization = specialization,
            location = location,
            isActive = true
        )

        when (val result = trainerRepository.createTrainer(req)) {
            is Result.Success -> {
                loadTrainer()
            }
            is Result.Error ->
                _event.emit(Trainer.Event.OnError("Помилка створення тренера: ${result.error}"))
        }
    }
}

