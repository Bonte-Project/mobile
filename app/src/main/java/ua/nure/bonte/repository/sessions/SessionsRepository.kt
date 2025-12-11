package ua.nure.bonte.repository.sessions

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.CreateSessionDto
import ua.nure.bonte.repository.dto.ResponseDto
import ua.nure.bonte.repository.dto.SessionDto
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.repository.dto.SessionsDto
import java.time.LocalDate
import java.time.LocalDateTime

interface SessionsRepository {
    suspend fun getUserSessions(): Result<SessionsDto, DataError>
    suspend fun getTrainerSessions(): Result<SessionsDto, DataError>
    suspend fun getTrainerSessionsByTrainerId(id: String): Result<SessionsDto, DataError>
    suspend fun createSession(name: String, userId: String, scheduledAt: LocalDateTime): Result<CreateSessionDto, DataError>
    suspend fun updateSession(id: String, name: String, scheduledAt: LocalDateTime, status: SessionStatus): Result<CreateSessionDto, DataError>
    suspend fun deleteSession(id: String): Result<ResponseDto, DataError>

    suspend fun getSessionsByDay(trainerId: String, day: LocalDate): Flow<List<SessionEntity>>
}