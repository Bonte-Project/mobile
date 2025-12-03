package ua.nure.bonte.repository.nutrition
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.db.data.entity.NutritionLogEntity
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.*
import ua.nure.bonte.repository.dto.*
import ua.nure.bonte.repository.dto.mapper.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class NutritionRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    @DbDeliveryDispatcher private val dbDispatcher: CoroutineDispatcher
) : NutritionRepository {

    override suspend fun getGoal(): Result<NutritionGoalRequest, DataError> = withContext(dbDispatcher) {
        safeCall<NutritionGoalDto> {
            httpClient.get("nutrition-goals")
        }.map { dto ->
            val entity = dto.toEntity()
            appDb.nutritionGoalDao.insert(entity)
            dto.toRequest()
        }
    }

    override suspend fun createOrUpdateGoals(request: NutritionGoalRequest): Result<Unit, DataError> =
        withContext(dbDispatcher) {
            safeCall<Unit> {
                httpClient.post("nutrition-goals") { setBody(request) }
            }
        }

    override suspend fun deleteGoals(): Result<Unit, DataError> = withContext(dbDispatcher) {
        safeCall<Unit> {
            httpClient.delete("nutrition-goals")
        }.onSuccess {
            appDb.nutritionGoalDao.clearAll()
        }
    }

    private val nutritionLogDao get() = appDb.nutritionLogDao

    override fun getLogs(): Flow<List<NutritionLogDto>> =
        nutritionLogDao.getAllLogs()
            .map { list -> list.map { it.toDto() } }
            .flowOn(dbDispatcher)

    override suspend fun getLogById(id: String): Result<NutritionLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionLogDto> {
                httpClient.get("nutrition-logs/$id")
            }.onSuccess { response ->
                nutritionLogDao.getLogById(id)?.let {
                    nutritionLogDao.insert(response.toEntity())
                }
            }
        }

    override suspend fun createLog(request: NutritionLogRequest): Result<NutritionLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionLogDto> {
                httpClient.post("nutrition-logs") { setBody(request) }
            }.onSuccess { response ->
                nutritionLogDao.insert(response.toEntity())
            }
        }

    override suspend fun updateLog(id: String, request: NutritionLogRequest): Result<NutritionLogDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionLogDto> {
                httpClient.patch("nutrition-logs/$id") { setBody(request) }
            }.onSuccess { response ->
                nutritionLogDao.insert(response.toEntity())
            }
        }

    override suspend fun deleteLog(id: String): Result<Unit, DataError> =
        withContext(dbDispatcher) {
            safeCall<Unit> {
                httpClient.delete("nutrition-logs/$id")
            }.onSuccess {
                nutritionLogDao.deleteById(id)
            }
        }

    override fun observeLocalLogs(): Flow<List<NutritionLogEntity>> =
        appDb.nutritionLogDao.getAllLogs()
            .catch { emit(emptyList()) }
            .flowOn(dbDispatcher)
}
