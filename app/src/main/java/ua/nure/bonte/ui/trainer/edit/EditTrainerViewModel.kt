package ua.nure.bonte.ui.trainer.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.trainer.TrainerRepository
import javax.inject.Inject

@HiltViewModel
class EditTrainerViewModel @Inject constructor(
    private val trainerRepository: TrainerRepository
) : ViewModel() {

    private val TAG by lazy { EditTrainerViewModel::class.simpleName }

    private val _state = MutableStateFlow(EditTrainer.State())
    val state: StateFlow<EditTrainer.State> = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditTrainer.Event>()
    val event = _event.asSharedFlow()

    init {
        loadTrainer()
    }

    private fun loadTrainer() = viewModelScope.launch {
        trainerRepository.loadMyTrainer()
            .onSuccess { trainerResponse ->
                val trainerEntity = TrainerEntity(
                    trainerId = trainerResponse.trainer.id,
                    userId = trainerResponse.trainer.userId,
                    bio = trainerResponse.trainer.bio,
                    certification = trainerResponse.trainer.certification,
                    specialization = trainerResponse.trainer.specialization,
                    location = trainerResponse.trainer.location,
                    isActive = trainerResponse.trainer.isActive
                )
                val experiences = trainerResponse.trainer.experience.map { exp ->
                    ExperienceEntity(
                        experienceId = exp.id,
                        trainerId = trainerEntity.trainerId,
                        title = exp.title,
                        description = exp.description,
                        startDate = exp.startDate,
                        endDate = exp.endDate
                    )
                }
                _state.update {
                    it.copy(
                        trainer = trainerEntity,
                        experiences = experiences
                    )
                }
            }.onError { e ->
                Log.e(TAG, "Failed to load trainer: $e")
            }
    }

    fun onAction(action: EditTrainer.Action) {
        when (action) {
            EditTrainer.Action.OnBack -> {
                viewModelScope.launch { _event.emit(EditTrainer.Event.OnBack) }
            }

            is EditTrainer.Action.OnNavigate -> {
                viewModelScope.launch { _event.emit(EditTrainer.Event.OnNavigate(action.route)) }
            }
            EditTrainer.Action.OnConfirm -> {
                val trainer = state.value.trainer ?: return
                _state.update { it.copy(inProgress = true) }

                viewModelScope.launch {
                    trainerRepository.updateTrainer(
                        TrainerRequest(
                            bio = trainer.bio,
                            certification = trainer.certification,
                            specialization = trainer.specialization,
                            location = trainer.location,
                            isActive = trainer.isActive
                        )
                    )
                        .onSuccess {
                            Log.d(TAG, "Trainer updated successfully via Confirm")
                            _state.update { it.copy(inProgress = false) }
                        }
                        .onError { e ->
                            Log.e(TAG, "Failed to update trainer: $e")
                            _state.update { it.copy(inProgress = false) }
                        }
                }
            }
            is EditTrainer.Action.OnBioChange -> {
                _state.update { s -> s.copy(trainer = s.trainer?.copy(bio = action.bio)) }
            }

            is EditTrainer.Action.OnCertificationChange -> {
                _state.update { s -> s.copy(trainer = s.trainer?.copy(certification = action.certification)) }
            }

            is EditTrainer.Action.OnSpecializationChange -> {
                _state.update { s -> s.copy(trainer = s.trainer?.copy(specialization = action.specialization)) }
            }

            is EditTrainer.Action.OnLocationChange -> {
                _state.update { s -> s.copy(trainer = s.trainer?.copy(location = action.location)) }
            }

            EditTrainer.Action.OnAddExperienceClick -> {
                _state.update { it.copy(showAddExperienceDialog = true) }
            }

            EditTrainer.Action.OnDismissAddExperienceDialog -> {
                _state.update { it.copy(showAddExperienceDialog = false) }
            }

            is EditTrainer.Action.OnSaveExperience -> {
                _state.update { it.copy(inProgress = true) }
                val expRequest = ExperienceRequest(
                    title = action.title,
                    description = action.description,
                    startDate = action.startDate,
                    endDate = action.endDate
                )
                viewModelScope.launch {
                    trainerRepository.addExperience(state.value.trainer?.trainerId ?: "", expRequest)
                        .onSuccess { response ->
                            val newExp = response.trainer.experience.last().let { exp ->
                                ExperienceEntity(
                                    experienceId = exp.id,
                                    trainerId = response.trainer.id,
                                    title = exp.title,
                                    description = exp.description,
                                    startDate = exp.startDate,
                                    endDate = exp.endDate
                                )
                            }
                            _state.update { s ->
                                s.copy(
                                    experiences = s.experiences + newExp,
                                    showAddExperienceDialog = false,
                                    inProgress = false
                                )
                            }
                        }
                        .onError { e ->
                            Log.e(TAG, "Failed to save experience: $e")
                            _state.update { it.copy(inProgress = false) }
                        }
                }
            }

            is EditTrainer.Action.OnExperienceTitleChange -> {
                updateExperienceField(action.experienceId, title = action.title)
            }

            is EditTrainer.Action.OnExperienceDescriptionChange -> {
                updateExperienceField(action.experienceId, description = action.description)
            }

            is EditTrainer.Action.OnExperienceDelete -> {
                _state.update { it.copy(inProgress = true) }
                viewModelScope.launch {
                    trainerRepository.deleteExperience(trainerId = state.value.trainer?.trainerId ?: "", action.experienceId)
                        .onSuccess {
                            _state.update { s ->
                                s.copy(
                                    experiences = s.experiences.filter { it.experienceId != action.experienceId },
                                    inProgress = false
                                )
                            }
                        }
                        .onError { e ->
                            Log.e(TAG, "Failed to delete experience: $e")
                            _state.update { it.copy(inProgress = false) }
                        }
                }
            }
        }
    }

    private fun updateExperienceField(
        experienceId: String,
        title: String? = null,
        description: String? = null
    ) {
        _state.update { s ->
            val updated = s.experiences.map { e ->
                if (e.experienceId == experienceId) {
                    e.copy(
                        title = title ?: e.title,
                        description = description ?: e.description
                    )
                } else e
            }
            s.copy(experiences = updated)
        }

        viewModelScope.launch {
            val exp = _state.value.experiences.first { it.experienceId == experienceId }
            trainerRepository.updateExperience(
                trainerId = state.value.trainer?.trainerId ?: "",
                experienceId = experienceId,
                request = ExperienceRequest(
                    title = exp.title ?: "",
                    description = exp.description,
                    startDate = exp.startDate,
                    endDate = exp.endDate
                )
            ).onSuccess {
                Log.d(TAG, "Experience updated: $experienceId")
            }.onError {
                Log.e(TAG, "Failed to update experience: $it")
            }
        }
    }

    fun patchTrainerOnFocusLost() {
        val trainer = _state.value.trainer ?: return
        viewModelScope.launch {
            trainerRepository.updateTrainer(
                TrainerRequest(
                    bio = trainer.bio,
                    certification = trainer.certification,
                    specialization = trainer.specialization,
                    location = trainer.location,
                    isActive = trainer.isActive
                )
            ).onSuccess {
                Log.d(TAG, "Trainer updated on focus lost")
            }.onError {
                Log.e(TAG, "Failed to update trainer: $it")
            }
        }
    }
}
