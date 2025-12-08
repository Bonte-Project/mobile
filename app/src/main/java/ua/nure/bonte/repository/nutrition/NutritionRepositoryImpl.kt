package ua.nure.bonte.repository.nutrition
import android.util.Log
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
import ua.nure.bonte.db.data.entity.NutritionGoalEntity


@OptIn(ExperimentalCoroutinesApi::class)
class NutritionRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    @DbDeliveryDispatcher private val dbDispatcher: CoroutineDispatcher
) : NutritionRepository {
    private val nutritionGoalDao get() = appDb.nutritionGoalDao

    override suspend fun getGoal(): Result<NutritionGoalRequest, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionGoalResponse> {
                httpClient.get("nutrition-goals")
            }.map { response ->
                val dto = response.goal
                nutritionGoalDao.insert(dto.toEntity())
                dto.toRequest()
            }
        }


    override suspend fun createOrUpdateGoals(request: NutritionGoalRequest): Result<NutritionGoalDto, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionGoalDto> {
                httpClient.post("nutrition-goals") { setBody(request) }
            }.onSuccess {
                val entity = request.toEntity()
                nutritionGoalDao.insert(entity)
            }
        }

    override suspend fun deleteGoals(): Result<Unit, DataError> = withContext(dbDispatcher) {
        safeCall<Unit> {
            httpClient.delete("nutrition-goals")
        }.onSuccess {
            nutritionGoalDao.clearAll()
        }
    }

    override fun observeLocalGoal(): Flow<NutritionGoalEntity?> =
        nutritionGoalDao.getGoal()
            .catch { emit(null) }
            .flowOn(dbDispatcher)

    private val nutritionLogDao get() = appDb.nutritionLogDao

    override fun getLogs(): Flow<List<NutritionLogDto>> =
        nutritionLogDao.getAllLogs()
            .map { list -> list.map { it.toDto() } }
            .flowOn(dbDispatcher)

    override suspend fun refreshNutritionLogs(): Result<Unit, DataError> =
        withContext(dbDispatcher) {
            safeCall<NutritionLogResponse> {
                httpClient.get("nutrition-logs")
            }.onSuccess { response ->
                nutritionLogDao.insertAll(
                    response.logs.map { it.toEntity() }
                )
            }.map { Unit }
        }


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
