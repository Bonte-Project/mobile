package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val message: String,
)
