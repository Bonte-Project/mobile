package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainerRequest(
    val bio: String,
    val certification: String,
    val specialization: String,
    val location: String,
    val isActive: Boolean
)
