package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetHistoryResponseDto(
    val message: String? = null,
    val messages: List<AiMessageDto>
)
