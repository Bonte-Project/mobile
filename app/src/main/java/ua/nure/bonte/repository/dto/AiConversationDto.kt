package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiConversationDto(
    val id: String,
    val createdAt: String,
    val systemPromptId: String? = null
)