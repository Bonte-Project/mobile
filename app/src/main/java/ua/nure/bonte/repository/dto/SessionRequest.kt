package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionRequest(
    val name: String,
    val userId: String? = null,
    val scheduledAt: String,
    val status: SessionStatus? = null,
)

@Serializable
data class SessionScheduleLongRequest(
    val name: String,
    val userId: String? = null,
    val scheduledAt: Long,
    val status: SessionStatus? = null,
)
