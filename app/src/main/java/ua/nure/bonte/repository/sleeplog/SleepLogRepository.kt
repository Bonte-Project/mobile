package ua.nure.bonte.repository.sleeplog

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.dto.CreateSleepLogDto
import ua.nure.bonte.repository.dto.UpdateSleepLogDto
import ua.nure.bonte.repository.dto.SleepLogDto

interface SleepLogRepository {
    fun getSleepLogs(): Flow<List<SleepLogDto>>
    suspend fun refreshSleepLogs(): Result<Unit, DataError>

    suspend fun getSleepLogById(id: String): Result<SleepLogDto, DataError>
    suspend fun createSleepLog(request: CreateSleepLogDto): Result<SleepLogDto, DataError>
    suspend fun updateSleepLog(id: String, request: UpdateSleepLogDto): Result<SleepLogDto, DataError>
    suspend fun deleteSleepLog(id: String): Result<Unit, DataError>
}
