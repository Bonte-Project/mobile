package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val message: String,
    val email: String,
)