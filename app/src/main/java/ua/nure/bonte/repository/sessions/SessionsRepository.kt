package ua.nure.bonte.repository.sessions

import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.CreateSessionDto
import ua.nure.bonte.repository.dto.ResponseDto
import ua.nure.bonte.repository.dto.SessionDto
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.repository.dto.SessionsDto

interface SessionsRepository {
    suspend fun getUserSessions(): Result<SessionsDto, DataError>
    suspend fun getTrainerSessions(): Result<SessionsDto, DataError>
    suspend fun getTrainerSessionsByTrainerId(id: String): Result<SessionsDto, DataError>
    suspend fun createSession(name: String, userId: String, scheduledAt: String): Result<CreateSessionDto, DataError>
    suspend fun updateSession(id: String, name: String, scheduledAt: String, status: SessionStatus): Result<CreateSessionDto, DataError>
    suspend fun deleteSession(id: String): Result<ResponseDto, DataError>
}