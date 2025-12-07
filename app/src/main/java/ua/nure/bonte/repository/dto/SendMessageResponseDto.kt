package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageResponseDto(
    val message: String? = null,
    val userMessage: AiMessageDto? = null,
    val aiResponse: AiMessageDto? = null
)