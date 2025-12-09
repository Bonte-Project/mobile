package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageSingleResponse(
    val message: String,
    val data: MessageDto
)

