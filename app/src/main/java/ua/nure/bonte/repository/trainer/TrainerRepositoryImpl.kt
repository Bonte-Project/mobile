package ua.nure.bonte.repository.trainer

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.db.data.entity.TrainerToExperienceEntity
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.dto.DeleteResponse
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.repository.dto.TrainerRequest
import ua.nure.bonte.repository.dto.TrainerResponse
import ua.nure.bonte.repository.dto.mapper.toTrainerResponse
import ua.nure.bonte.repository.safeCall
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class TrainerRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CoroutineDispatcher
) : TrainerRepository {

    override suspend fun loadTrainer(): Result<TrainerResponse, DataError> = withContext(dbDeliveryDispatcher) {
        safeCall<TrainerResponse> {
            httpClient.get("trainers/me")
        }.onSuccess { response ->
            val t = response.trainer
            val trainerEntity = TrainerEntity(
                trainerId = t.id,
                userId = t.userId,
                bio = t.bio,
                certification = t.certification,
                specialization = t.specialization,
                location = t.location,
                isActive = t.isActive
            )
            appDb.trainerDao.insert(trainerEntity)

            val trainerToExpList = t.experience.map { exp ->
                val expEntity = ExperienceEntity(
                    experienceId = exp.id,
                    trainerId = trainerEntity.trainerId,
                    title = exp.title,
                    description = exp.description,
                    startDate = exp.startDate,
                    endDate = exp.endDate
                )
                appDb.experienceDao.insert(expEntity)
                TrainerToExperienceEntity(trainerEntity.trainerId, exp.id)
            }
            if (trainerToExpList.isNotEmpty()) {
                appDb.trainerToExperienceDao.insert(trainerToExpList)
            }
        }
    }

    override suspend fun createTrainer(request: TrainerRequest): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            safeCall<TrainerResponse> {
                httpClient.post("trainers") {
                    setBody(request)
                }
            }.onSuccess { response ->
                val t = response.trainer
                val trainerEntity = TrainerEntity(
                    trainerId = t.id,
                    userId = t.userId,
                    bio = t.bio,
                    certification = t.certification,
                    specialization = t.specialization,
                    location = t.location,
                    isActive = t.isActive
                )
                appDb.trainerDao.insert(trainerEntity)

                val trainerToExpList = t.experience.map { exp ->
                    val expEntity = ExperienceEntity(
                        experienceId = exp.id,
                        trainerId = trainerEntity.trainerId,
                        title = exp.title,
                        description = exp.description,
                        startDate = exp.startDate,
                        endDate = exp.endDate
                    )
                    appDb.experienceDao.insert(expEntity)
                    TrainerToExperienceEntity(trainerEntity.trainerId, exp.id)
                }
                if (trainerToExpList.isNotEmpty()) {
                    appDb.trainerToExperienceDao.insert(trainerToExpList)
                }
            }
        }

    override suspend fun updateTrainer(request: TrainerRequest): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            safeCall<TrainerResponse> {
                httpClient.put("trainers/me") {
                    setBody(request)
                }
            }.onSuccess { response ->
                val t = response.trainer
                val trainerEntity = TrainerEntity(
                    trainerId = t.id,
                    userId = t.userId,
                    bio = t.bio,
                    certification = t.certification,
                    specialization = t.specialization,
                    location = t.location,
                    isActive = t.isActive
                )
                appDb.trainerDao.insert(trainerEntity)

                val trainerToExpList = t.experience.map { exp ->
                    val expEntity = ExperienceEntity(
                        experienceId = exp.id,
                        trainerId = trainerEntity.trainerId,
                        title = exp.title,
                        description = exp.description,
                        startDate = exp.startDate,
                        endDate = exp.endDate
                    )
                    appDb.experienceDao.insert(expEntity)
                    TrainerToExperienceEntity(trainerEntity.trainerId, exp.id)
                }
                if (trainerToExpList.isNotEmpty()) {
                    appDb.trainerToExperienceDao.insert(trainerToExpList)
                }
            }
        }

    override suspend fun getTrainerById(id: String): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            safeCall<TrainerResponse> {
                httpClient.get("trainers/$id")
            }
        }

    override suspend fun addExperience(request: ExperienceRequest): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            safeCall<TrainerResponse> {
                httpClient.post("trainers/me/experience") {
                    setBody(request)
                }
            }.onSuccess { response ->
                val trainer = appDb.trainerDao.getTrainer().first()
                val exp = response.trainer.experience.last()
                val expEntity = ExperienceEntity(
                    experienceId = exp.id,
                    trainerId = trainer.trainerId,
                    title = exp.title,
                    description = exp.description,
                    startDate = exp.startDate,
                    endDate = exp.endDate
                )
                appDb.experienceDao.insert(expEntity)
                appDb.trainerToExperienceDao.insert(listOf(TrainerToExperienceEntity(trainer.trainerId, expEntity.experienceId)))
            }
        }

    override suspend fun updateExperience(experienceId: String, request: ExperienceRequest): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            safeCall<TrainerResponse> {
                httpClient.put("trainers/me/experience/$experienceId") {
                    setBody(request)
                }
            }.onSuccess { response ->
                val trainer = appDb.trainerDao.getTrainer().first()
                val expEntity = ExperienceEntity(
                    experienceId = experienceId,
                    trainerId = trainer.trainerId,
                    title = request.title,
                    description = request.description,
                    startDate = request.startDate,
                    endDate = request.endDate
                )
                appDb.experienceDao.insert(expEntity)
                appDb.trainerToExperienceDao.insert(listOf(TrainerToExperienceEntity(trainer.trainerId, experienceId)))
            }
        }

    override suspend fun deleteExperience(experienceId: String): Result<TrainerResponse, DataError> =
        withContext(dbDeliveryDispatcher) {
            val deleteResult = safeCall<DeleteResponse> {
                httpClient.delete("trainers/me/experience/$experienceId")
            }

            return@withContext when (deleteResult) {
                is Result.Success -> {
                    val trainer = appDb.trainerDao.getTrainer().first()
                    appDb.experienceDao.deleteById(experienceId)
                    appDb.trainerToExperienceDao.deleteByExperienceId(experienceId)
                    val trainerEntity = trainer
                    val experiences = appDb.experienceDao.getExperience().first().filter { it.trainerId == trainerEntity.trainerId }
                    Result.Success(trainerEntity.toTrainerResponse(experiences))
                }
                is Result.Error -> {
                    Result.Error(deleteResult.error)
                }
            }
        }

    override fun getTrainer(): Flow<TrainerResponse> =
        appDb.trainerDao.getTrainer().flatMapLatest { trainer ->
            appDb.experienceDao.getExperience().map { experiences ->
                trainer.toTrainerResponse(experiences.filter { it.trainerId == trainer.trainerId })
            }
        }.flowOn(dbDeliveryDispatcher).catch { it.printStackTrace() }
}
