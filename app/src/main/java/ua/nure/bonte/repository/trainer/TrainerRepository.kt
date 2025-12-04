package ua.nure.bonte.repository.trainer

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.TrainerListResponse

interface TrainerRepository {
    suspend fun createTrainer(request: TrainerRequest): Result<TrainerResponse, DataError>
    suspend fun updateTrainer(request: TrainerRequest): Result<TrainerResponse, DataError>
    suspend fun loadMyTrainer(): Result<TrainerResponse, DataError>
    suspend fun loadTrainers(): Result<TrainerListResponse, DataError>
    suspend fun getTrainerById(id: String): Result<TrainerResponse, DataError>
    suspend fun addExperience(request: ExperienceRequest): Result<TrainerResponse, DataError>
    suspend fun updateExperience(experienceId: String, request: ExperienceRequest): Result<TrainerResponse, DataError>
    suspend fun deleteExperience(experienceId: String): Result<TrainerResponse, DataError>
    fun getTrainers(): Flow<List<Trainer>>
}

