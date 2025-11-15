package ua.nure.bonte.repository.user

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.db.DbRepository
import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.ProfileDataDto
import ua.nure.bonte.repository.dto.ProfileDto
import ua.nure.bonte.repository.dto.UserProfileRequest
import ua.nure.bonte.repository.dto.mapper.toEntity
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val httpClient: HttpClient,
    private val dbRepository: DbRepository,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : UserRepository {
    override suspend fun loadMe(): Result<ProfileDataDto, DataError> = withContext(Dispatchers.IO) {
        val profile = dbRepository.db.profileDao.getProfileEntity()
        if (profile == null) {
            safeCall<ProfileDataDto> {
                httpClient.get("users/me") {

                }
            }.onSuccess { profileDataDto ->
                dbRepository.db.profileDao
                    .insert(
                        profileDataDto.user.toEntity()
                    )
            }
        } else {
            Result.Success (ProfileDataDto("", user = ProfileDto(
                id = profile.id,
                email = profile.email ?: "",
                fullName = profile.fullName ?: "",
                avatarUrl = profile.avatarUrl,
                role = profile.role,
                isEmailVerified = profile.isEmailVerified,
                height = profile.height,
                weight = profile.weight,
                age = profile.age,
                createdAt = profile.createdAt,
                isPremium = profile.isPremium
            )))
        }
    }

    override suspend fun patchMe(
        firstName: String,
        lastName: String,
        height: Int?,
        weight: Int?,
        age: Int?,
        avatarUrl: String?
    ): Result<ProfileDataDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<ProfileDataDto> {
            httpClient.patch("users/me") {
                setBody(
                    UserProfileRequest(
                        fullName = "$firstName $lastName",
                        avatarUrl = avatarUrl,
                        height = height,
                        weight = weight,
                        age = age

                    )
                )

            }
        }.onSuccess {
            dbRepository.db.profileDao.insert(it.user.toEntity())
        }
    }


    override fun getMe(): Flow<ProfileEntity> =
        dbRepository
            .dbFlow
            .flatMapLatest { db ->  db.profileDao.getProfile() }
            .flowOn(dbDeliveryDispatcher)
            .catch {
                it.printStackTrace()
            }
}