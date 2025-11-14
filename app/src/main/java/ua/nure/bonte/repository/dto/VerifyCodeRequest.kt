package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyCodeRequest(
    val email: String,
    val code: String
)