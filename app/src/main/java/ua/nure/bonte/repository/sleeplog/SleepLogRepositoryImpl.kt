package ua.nure.bonte.repository.sleeplog

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.db.data.entity.SleepLogEntity
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall
import ua.nure.bonte.repository.dto.CreateSleepLogDto
import ua.nure.bonte.repository.dto.UpdateSleepLogDto
import ua.nure.bonte.repository.dto.SleepLogDto
import ua.nure.bonte.repository.dto.mapper.toDto
import ua.nure.bonte.repository.dto.mapper.toEntity
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SleepLogRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    @DbDeliveryDispatcher private val dbDispatcher: CoroutineDispatcher
) : SleepLogRepository {

    private val sleepLogDao get() = appDb.sleepLogDao

    override fun getSleepLogs(): Flow<List<SleepLogDto>> =
        sleepLogDao.getAllSleepLogs()
            .map { list -> list.map { it.toDto() } }
            .flowOn(dbDispatcher)

    override suspend fun getSleepLogById(id: String): Result<SleepLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<SleepLogDto> {
                httpClient.get("sleep-logs/$id")
            }.onSuccess { response ->
                sleepLogDao.getSleepLogById(id)?.let {
                    sleepLogDao.updateSleepLog(response.toEntity())
                }
            }
        }

    override suspend fun createSleepLog(request: CreateSleepLogDto): Result<SleepLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<SleepLogDto> {
                httpClient.post("sleep-logs") { setBody(request) }
            }.onSuccess { response ->
                sleepLogDao.insertSleepLog(response.toEntity())
            }
        }

    override suspend fun updateSleepLog(id: String, request: UpdateSleepLogDto): Result<SleepLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<SleepLogDto> {
                httpClient.patch("sleep-logs/$id") { setBody(request) }
            }.onSuccess { response ->
                sleepLogDao.updateSleepLog(response.toEntity())
            }
        }

    override suspend fun deleteSleepLog(id: String): Result<Unit, DataError> =
        withContext(dbDispatcher) {
            safeCall<Unit> {
                httpClient.delete("sleep-logs/$id")
            }.onSuccess {
                sleepLogDao.deleteById(id)
            }
        }
}
