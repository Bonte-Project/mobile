package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyResetCodeRequest(
    val email: String,
    val code: String
)
