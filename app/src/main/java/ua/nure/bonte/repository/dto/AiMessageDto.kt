package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class AiMessageDto(
    val id: String,
    val message: String,
    val sentAt: String,
    val toFrom: Boolean,
    val sender: String? = null,
    val index: Int,
    val isSystem: Boolean = false
)
