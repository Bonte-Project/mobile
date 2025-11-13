package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerificationCodeRequest(
    val email: String
)

@Serializable
data class VerifyCodeRequest(
    val email: String,
    val code: String
)