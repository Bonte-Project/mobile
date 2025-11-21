package ua.nure.bonte.repository.trainer

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result

interface TrainerRepository {
    suspend fun createTrainer(request: TrainerRequest): Result<TrainerResponse, DataError>
    suspend fun updateTrainer(request: TrainerRequest): Result<TrainerResponse, DataError>
    suspend fun loadTrainer(): Result<TrainerResponse, DataError>
    suspend fun getTrainerById(id: String): Result<TrainerResponse, DataError>
    suspend fun addExperience(request: ExperienceRequest): Result<TrainerResponse, DataError>
    suspend fun updateExperience(experienceId: String, request: ExperienceRequest): Result<TrainerResponse, DataError>
    suspend fun deleteExperience(experienceId: String): Result<TrainerResponse, DataError>
    fun getTrainer(): Flow<TrainerResponse>
}

