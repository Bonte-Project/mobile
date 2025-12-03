package ua.nure.bonte.repository.activity

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.ActivityLogDto
import ua.nure.bonte.db.data.entity.ActivityLogEntity
import ua.nure.bonte.repository.dto.ActivityLogRequest

interface ActivityRepository {

    fun getActivityLogs(): Flow<List<ActivityLogDto>>
    suspend fun getActivityLogById(id: String): Result<ActivityLogDto, DataError>

    suspend fun createActivityLog(request: ActivityLogRequest): Result<ActivityLogDto, DataError>
    suspend fun updateActivityLog(
        id: String,
        request: ActivityLogDto
    ): Result<ActivityLogDto, DataError>

    suspend fun deleteActivityLog(id: String): Result<Unit, DataError>
    fun observeLocalLogs(): Flow<List<ActivityLogEntity>>
}