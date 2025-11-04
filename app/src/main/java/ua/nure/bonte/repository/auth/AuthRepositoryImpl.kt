package ua.nure.bonte.repository.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.MessageDto
import ua.nure.bonte.repository.dto.RegisterRequest
import ua.nure.bonte.repository.safeCall

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
) : AuthRepository {
    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: String
    ): Result<MessageDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<MessageDto> {
            httpClient.post("/auth/register") {
                setBody(
                    body = RegisterRequest(
                        fullName = fullName,
                        email = email,
                        password = password,
                        role = role,
                    )
                )
            }
        }
    }
}