package ua.nure.bonte.repository.auth

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.MessageDto

interface AuthRepository {
    suspend fun register(fullName: String, email: String, password: String, role: String): Result<MessageDto, DataError>
    fun getProfile(): Flow<ProfileEntity>
}