package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val message: String,
    val to_from: Boolean
)
