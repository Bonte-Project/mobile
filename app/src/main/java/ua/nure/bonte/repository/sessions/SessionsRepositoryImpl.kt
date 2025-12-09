package ua.nure.bonte.repository.sessions

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.DbRepository
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.di.DbDeliveryDispatcher
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.CreateSessionDto
import ua.nure.bonte.repository.dto.ResponseDto
import ua.nure.bonte.repository.dto.SessionRequest
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.repository.dto.SessionsDto
import ua.nure.bonte.repository.dto.mapper.toEntity
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class SessionsRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val dbRepository: DbRepository,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CoroutineDispatcher
) : SessionsRepository {
    private val TAG by lazy { SessionsRepositoryImpl::class.simpleName }

    override suspend fun getUserSessions(): Result<SessionsDto, DataError> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "getUserSessions: ...")



            safeCall<SessionsDto> {
                httpClient.get("training-sessions")
            }.onSuccess { sessionsDto ->
                Log.d(TAG, "getUserSessions: ${sessionsDto.sessions.size}")
                dbRepository.db.sessionDao.insert(
                    sessionsDto.sessions.map { it.toEntity() }
                )
            }
        }

    override suspend fun getTrainerSessions(): Result<SessionsDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<SessionsDto> {
                httpClient.get("training-sessions/trainer")
            }.onSuccess { sessionsDto ->
                dbRepository.db.sessionDao.insert(
                    sessionsDto.sessions.map { it.toEntity() }
                )
            }
        }

    override suspend fun createSession(
        name: String,
        userId: String,
        scheduledAt: String
    ): Result<CreateSessionDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<CreateSessionDto> {
                httpClient.post("training-sessions") {
                    setBody(
                        SessionRequest(
                            name = name,
                            userId = userId,
                            scheduledAt = scheduledAt,
                        )
                    )
                }
            }.onSuccess { sessionDto ->
                dbRepository.db.sessionDao.insert(listOf(sessionDto.session.toEntity()))
            }
    }

    override suspend fun updateSession(
        id: String,
        name: String,
        scheduledAt: String,
        status: SessionStatus
    ): Result<CreateSessionDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<CreateSessionDto> {
                httpClient.patch("training-sessions/$id") {
                    setBody(
                        SessionRequest(
                            name = name,
                            status = status,
                            scheduledAt = scheduledAt,
                        )
                    )
                }
            }.onSuccess { sessionDto ->
                dbRepository.db.sessionDao.insert(listOf(sessionDto.session.toEntity()))
            }
        }

    override suspend fun deleteSession(id: String): Result<ResponseDto, DataError> =
        withContext(Dispatchers.IO) {
        safeCall<ResponseDto> {
            httpClient.delete("/training-sessions/$id")
        }.onSuccess {
            dbRepository.db.sessionDao.deleteById(id = id)
        }
    }

}