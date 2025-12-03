package ua.nure.bonte.repository.activity

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.ActivityLogDto
import ua.nure.bonte.repository.dto.ActivityLogRequest
import ua.nure.bonte.repository.dto.ActivityLogResponse
import ua.nure.bonte.repository.dto.mapper.toDto
import ua.nure.bonte.repository.dto.mapper.toEntity
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall
import io.ktor.client.request.*
import kotlinx.coroutines.flow.*
import ua.nure.bonte.repository.*
import ua.nure.bonte.repository.dto.*
import ua.nure.bonte.repository.dto.mapper.*
import ua.nure.bonte.db.data.entity.ActivityLogEntity

class ActivityRepositoryImpl(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    @DbDeliveryDispatcher private val dbDispatcher: CoroutineDispatcher
): ActivityRepository {
    private val activityLogDao get() = appDb.activityLogDao

    override fun getActivityLogs(): Flow<List<ActivityLogDto>> =
        activityLogDao.getAllLogs()
            .map { list -> list.map { it.toDto() } }
            .flowOn(dbDispatcher)

    override suspend fun getActivityLogById(id: String): Result<ActivityLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<ActivityLogDto> {
                httpClient.get("activity-logs/$id")
            }.onSuccess { response ->
                activityLogDao.getLogById(id)?.let {
                    activityLogDao.updateActivityLog(response.toEntity())
                }
            }
        }

    override suspend fun createActivityLog(request: ActivityLogRequest): Result<ActivityLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<ActivityLogDto> {
                httpClient.post("activity-logs") { setBody(request) }
            }.onSuccess { response ->
                activityLogDao.insert(response.toEntity())
            }
        }

    override suspend fun updateActivityLog(id: String, request: ActivityLogDto): Result<ActivityLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<ActivityLogDto> {
                httpClient.patch("activity-logs/$id") { setBody(request) }
            }.onSuccess { response ->
                activityLogDao.updateActivityLog(response.toEntity())
            }
        }

    override suspend fun deleteActivityLog(id: String): Result<Unit, DataError> =
        withContext(dbDispatcher) {
            safeCall<Unit> {
                httpClient.delete("activity-logs/$id")
            }.onSuccess {
                activityLogDao.deleteById(id)
            }
        }
    override fun observeLocalLogs(): Flow<List<ActivityLogEntity>> =
        appDb.activityLogDao.getAllLogs()
            .catch { emit(emptyList()) }
            .flowOn(dbDispatcher)

}