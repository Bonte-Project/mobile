package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateChatResponseDto(
    val message: String,
    val conversation: AiConversationDto? = null
)