package ua.nure.bonte.repository.auth

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.post
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
import ua.nure.bonte.repository.dto.MessageDto
import ua.nure.bonte.repository.dto.RegisterRequest
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall
import ua.nure.bonte.repository.token.ProfileRepository

class AuthRepositoryImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val httpClient: HttpClient,
    private val dbRepository: DbRepository,
    private val profileRepository: ProfileRepository,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : AuthRepository {
    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: String
    ): Result<MessageDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<MessageDto> {
            httpClient.post("auth/register") {
                setBody(
                    body = RegisterRequest(
                        fullName = fullName,
                        email = email,
                        password = password,
                        role = role,
                    )
                )
            }
        }.onSuccess {
            // save profile to db and profileRepository
//            dbRepository.db.profileDao.insert()
//            profileRepository.setToken()
//            profileRepository.setUserName("TestUser")
        }.onError {

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProfile(): Flow<ProfileEntity> =
        dbRepository.dbFlow
            .flatMapLatest { db -> db.profileDao.getProfile() }
            .flowOn(dbDeliveryDispatcher)
            .catch { it.printStackTrace() }
}