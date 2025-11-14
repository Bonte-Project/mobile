package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val newPassword: String,
)
