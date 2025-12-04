package ua.nure.bonte.repository.nutrition

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.NutritionGoalEntity
import ua.nure.bonte.db.data.entity.NutritionLogEntity
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.NutritionGoalDto
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.repository.dto.NutritionLogDto
import ua.nure.bonte.repository.dto.NutritionLogRequest
import ua.nure.bonte.repository.dto.NutritionLogResponse

interface NutritionRepository {

    suspend fun getGoal(): Result<NutritionGoalRequest, DataError>
    suspend fun createOrUpdateGoals(request: NutritionGoalRequest): Result<NutritionGoalDto, DataError>
    suspend fun deleteGoals(): Result<Unit, DataError>
    fun observeLocalGoal(): Flow<NutritionGoalEntity?>

    fun getLogs(): Flow<List<NutritionLogDto>>
    suspend fun getLogById(id: String): Result<NutritionLogDto, DataError>
    suspend fun createLog(request: NutritionLogRequest): Result<NutritionLogDto, DataError>
    suspend fun updateLog(id: String, request: NutritionLogRequest): Result<NutritionLogDto, DataError>
    suspend fun deleteLog(id: String): Result<Unit, DataError>

    fun observeLocalLogs(): Flow<List<NutritionLogEntity>>
}