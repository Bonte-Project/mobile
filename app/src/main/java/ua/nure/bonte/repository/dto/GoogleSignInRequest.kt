package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoogleSignInRequest(
    val token: String,
    val email: String? = null,
)
