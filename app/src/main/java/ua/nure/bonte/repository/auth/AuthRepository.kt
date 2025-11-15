package ua.nure.bonte.repository.auth

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.db.data.entity.ProfileEntity
import ua.nure.bonte.repository.dto.GoogleSignInDto
import ua.nure.bonte.repository.dto.ResponseDto
import ua.nure.bonte.repository.dto.SignInDto

interface AuthRepository {
    suspend fun register(fullName: String, email: String, password: String, role: String): Result<ResponseDto, DataError>
    fun getProfile(): Flow<ProfileEntity>
    suspend fun googleSignIn(token: String, email: String): Result<GoogleSignInDto, DataError>
    suspend fun signIn(email: String, password: String): Result<SignInDto, DataError>
    suspend fun forgotPassword(email: String): Result<ResponseDto, DataError>
    suspend fun verifyCode(email: String, code: String): Result<ResponseDto, DataError>
    suspend fun resetPassword(email: String, newPassword: String): Result<ResponseDto, DataError>
    suspend fun verifyEmail(email: String, code: String): Result<ResponseDto, DataError>
}