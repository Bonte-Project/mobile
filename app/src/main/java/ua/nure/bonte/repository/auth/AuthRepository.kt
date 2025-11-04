package ua.nure.bonte.repository.auth

import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.MessageDto

interface AuthRepository {
    suspend fun register(fullName: String, email: String, password: String, role: String): Result<MessageDto, DataError>
}