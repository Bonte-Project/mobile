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
import ua.nure.bonte.repository.dto.ForgotPasswordRequest
import ua.nure.bonte.repository.dto.GoogleSignInDto
import ua.nure.bonte.repository.dto.GoogleSignInRequest
import ua.nure.bonte.repository.dto.ResponseDto
import ua.nure.bonte.repository.dto.RegisterRequest
import ua.nure.bonte.repository.dto.ResetPasswordRequest
import ua.nure.bonte.repository.dto.SignInDto
import ua.nure.bonte.repository.dto.SignInRequest
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.repository.safeCall
import ua.nure.bonte.repository.token.TokenRepository
import ua.nure.bonte.repository.dto.VerifyCodeRequest

class AuthRepositoryImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val httpClient: HttpClient,
    private val dbRepository: DbRepository,
    private val tokenRepository: TokenRepository,
    @DbDeliveryDispatcher private val dbDeliveryDispatcher: CloseableCoroutineDispatcher,
) : AuthRepository {
    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: String
    ): Result<ResponseDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<ResponseDto> {
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

    override suspend fun googleSignIn(
        token: String,
        email: String
    ): Result<GoogleSignInDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<GoogleSignInDto> {
            httpClient.post("auth/google") {
                setBody(
                    GoogleSignInRequest(
                        token = token,
                        email = email
                    )
                )
            }
        }.onSuccess {
            tokenRepository.setToken(newToken = it.token)
            tokenRepository.setUserName(newUserName = email)
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<SignInDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<SignInDto> {
            httpClient.post("auth/login") {
                setBody(
                    SignInRequest(
                        email = email,
                        password = password
                    )
                )
            }
        }.onSuccess {
            tokenRepository.setToken(newToken = it.accessToken)
            tokenRepository.setUserName(newUserName = email)
        }
    }

    override suspend fun forgotPassword(email: String): Result<ResponseDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<ResponseDto> {
                httpClient.post("auth/forgot-password") {
                    setBody(
                        ForgotPasswordRequest(
                            email = email
                        )
                    )
                }
            }
        }

    override suspend fun verifyCode(email: String, code: String): Result<ResponseDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<ResponseDto> {
                httpClient.post("auth/verify-reset-code") {
                    setBody(
                        VerifyCodeRequest(email = email, code = code)
                    )
                }
            }
        }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): Result<ResponseDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<ResponseDto> {
            httpClient.post("auth/reset-password") {
                setBody(
                    ResetPasswordRequest(email = email, newPassword = newPassword)
                )
            }
        }
    }

    override suspend fun verifyEmail(
        email: String,
        code: String
    ): Result<ResponseDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<ResponseDto> {
            httpClient.post("auth/verify-email") {
                setBody(
                    VerifyCodeRequest(
                        email = email,
                        code = code
                    )
                )
            }

        }
    }


}