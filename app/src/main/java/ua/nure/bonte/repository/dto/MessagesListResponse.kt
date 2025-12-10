package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessagesListResponse(
    val message: String,
    val data: List<MessageDto>
)