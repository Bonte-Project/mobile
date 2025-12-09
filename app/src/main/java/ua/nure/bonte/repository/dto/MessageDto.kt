package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: String,
    val user_id: String,
    val trainer_id: String,
    val message: String,
    val sent_at: String,
    val to_from: Boolean
)