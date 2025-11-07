package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    val message: String,
    val accessToken: String,
)
