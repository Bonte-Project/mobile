package ua.nure.bonte.repository.token

interface TokenRepository {
    val token: String?
    suspend fun setToken(newToken: String?)
}