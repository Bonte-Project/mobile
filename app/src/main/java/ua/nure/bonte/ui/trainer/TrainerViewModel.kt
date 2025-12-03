package ua.nure.bonte.ui.trainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.db.data.entity.Trainer as TrainerDb
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.trainer.Trainer.Action
import ua.nure.bonte.ui.trainer.Trainer.Event
import ua.nure.bonte.ui.trainer.Trainer.State
import javax.inject.Inject

import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.repository.dto.ExperienceDto


@HiltViewModel
class TrainerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = State()
    )

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _trainerState = MutableStateFlow<TrainerResponse?>(null)
    private var trainerLoaded = false

    init {
        observeProfile()
    }

    private fun List<ExperienceDto>.toExperienceEntityList(trainerId: String): List<ExperienceEntity> {
        return this.map { dto ->
            ExperienceEntity(
                experienceId = dto.id,
                trainerId = trainerId,
                title = dto.title,
                description = dto.description,
                startDate = dto.startDate,
                endDate = dto.endDate
            )
        }
    }

    private fun updateLocalTrainer(update: (TrainerEntity) -> TrainerEntity) {
        _state.update { s ->
            val trainer = s.profile?.trainer ?: return@update s

            s.copy(
                profile = s.profile.copy(
                    trainer = trainer.copy(
                        trainerEntity = update(trainer.trainerEntity)
                    )
                )
            )
        }
    }

    private fun updateLocalExperience(id: String, update: (ExperienceEntity) -> ExperienceEntity) {
        _state.update { s ->
            val trainer = s.profile?.trainer ?: return@update s

            val currentExperience = trainer.experience?.toMutableList() ?: mutableListOf()

            val index = currentExperience.indexOfFirst { it.experienceId == id }

            if (index != -1) {
                val updatedItem = update(currentExperience[index])
                currentExperience[index] = updatedItem
            }

            s.copy(
                profile = s.profile.copy(
                    trainer = trainer.copy(
                        experience = currentExperience.toList()
                    )
                )
            )
        }
    }

    fun onAction(action: Action) = viewModelScope.launch {
        when (action) {
            Action.OnBack -> _event.emit(Event.OnBack)
            is Action.OnNavigate -> _event.emit(Event.OnNavigate(action.route))

            Action.LoadTrainer -> loadTrainer()

            is Action.OnBioChange ->
                updateLocalTrainer { it.copy(bio = action.bio) }

            is Action.OnCertificationChange ->
                updateLocalTrainer { it.copy(certification = action.certification) }

            is Action.OnSpecializationChange ->
                updateLocalTrainer { it.copy(specialization = action.specialization) }

            is Action.OnLocationChange ->
                updateLocalTrainer { it.copy(location = action.location) }

            Action.OnSaveProfile -> saveProfile()
            Action.OnCreateTrainer -> {}
            Action.OnDeleteProfile -> {}

            Action.OnAddExperience -> {}
            is Action.OnDeleteExperience -> deleteExperience(action.experienceId)
            is Action.OnExperienceChange -> updateExperienceLocallyAndRemotely(action)
            is Action.OnAddExperienceWithData -> addExperience(action.request)
        }
    }
    private fun updateExperienceLocallyAndRemotely(a: Action.OnExperienceChange) {
        updateLocalExperience(a.experienceId) { item ->
            item.copy(
                title = a.title,
                description = a.description,
                startDate = a.startDate,
                endDate = a.endDate
            )
        }
    }


    private fun observeProfile() = viewModelScope.launch {
        userRepository.getMe().collectLatest { profile ->
            _state.update { it.copy(profile = profile) }

            if (!trainerLoaded) {
                trainerLoaded = true
                loadTrainer()
            }
        }
    }

    fun loadTrainer() = viewModelScope.launch {
        when (val result = trainerRepository.loadTrainer()) {
            is Result.Success -> {
                val trainerResponse = result.data

                _state.update { s ->
                    val profile = s.profile ?: return@update s
                    val oldTrainer = profile.trainer

                    val entity = (oldTrainer?.trainerEntity ?: TrainerEntity(
                        trainerId = trainerResponse.trainer.id,
                        userId = profile.profileEntity.id,
                        bio = "",
                        certification = "",
                        specialization = "",
                        location = "",
                        isActive = trainerResponse.trainer.isActive
                    )).copy(
                        isActive = trainerResponse.trainer.isActive
                    )

                    val freshExperienceEntities = trainerResponse.trainer.experience?.toExperienceEntityList(
                        trainerId = trainerResponse.trainer.id
                    ) ?: emptyList()

                    val trainer = TrainerDb(
                        trainerEntity = entity,
                        experience = freshExperienceEntities
                    )

                    s.copy(profile = profile.copy(trainer = trainer))
                }
            }

            is Result.Error -> {
                if (result.error is DataError.Remote) {
                    return@launch
                }
                _event.emit(Event.OnError("Помилка завантаження тренера: ${result.error}"))
            }
        }
    }

    private fun saveProfile() = viewModelScope.launch {
        val profile = _state.value.profile ?: return@launch
        val trainerEntity = profile.trainer?.trainerEntity ?: return@launch
        val experienceList = profile.trainer.experience ?: emptyList()
        val mainReq = TrainerRequest(
            bio = trainerEntity.bio,
            certification = trainerEntity.certification,
            specialization = trainerEntity.specialization,
            location = trainerEntity.location,
            isActive = trainerEntity.isActive
        )
        updateTrainer(mainReq)
        experienceList.forEach { expEntity ->
            val expRequest = ExperienceRequest(
                title = expEntity.title,
                description = expEntity.description,
                startDate = expEntity.startDate,
                endDate = expEntity.endDate
            )
            updateExperience(expEntity.experienceId, expRequest)
        }
        loadTrainer()
    }


    fun addExperience(r: ExperienceRequest) = viewModelScope.launch {
        when (val result = trainerRepository.addExperience(r)) {
            is Result.Success -> {
                loadTrainer()
            }
            is Result.Error -> _event.emit(Event.OnError("Помилка додавання досвіду: ${result.error}"))
        }
    }

    fun updateExperience(id: String, r: ExperienceRequest) = viewModelScope.launch {
        when (val result = trainerRepository.updateExperience(id, r)) {
            is Result.Success -> {
            }
            is Result.Error -> _event.emit(Event.OnError("Помилка оновлення досвіду: ${result.error}"))
        }
    }

    fun deleteExperience(id: String) = viewModelScope.launch {
        when (val result = trainerRepository.deleteExperience(id)) {
            is Result.Success -> loadTrainer()
            is Result.Error -> _event.emit(Event.OnError("Помилка видалення досвіду: ${result.error}"))
        }
    }

    fun createTrainer(
        bio: String,
        certification: String,
        specialization: String,
        experience: List<ExperienceRequest>,
        location: String
    ) = viewModelScope.launch {

        val req = TrainerRequest(
            bio = bio,
            certification = certification,
            specialization = specialization,
            location = location,
            isActive = true
        )

        when (val result = trainerRepository.createTrainer(req)) {
            is Result.Success -> {
                _trainerState.value = result.data
                loadTrainer()

                experience.forEach { addExperience(it) }
            }
            is Result.Error ->
                _event.emit(Event.OnError("Помилка створення тренера: ${result.error}"))
        }
    }

    fun updateTrainer(req: TrainerRequest) = viewModelScope.launch {
        when (val result = trainerRepository.updateTrainer(req)) {
            is Result.Success -> {
                _trainerState.value = result.data
                loadTrainer()
            }

            is Result.Error ->
                _event.emit(Event.OnError("Помилка оновлення тренера: ${result.error}"))
        }
    }

    fun getTrainerById(id: String) = viewModelScope.launch {
        when (val result = trainerRepository.getTrainerById(id)) {
            is Result.Success -> _trainerState.value = result.data
            is Result.Error ->
                _event.emit(Event.OnError("Помилка отримання тренера: ${result.error}"))
        }
    }

    fun observeTrainerFlow() = viewModelScope.launch {
        trainerRepository.getTrainer().collectLatest {
            _trainerState.value = it
        }
    }
}