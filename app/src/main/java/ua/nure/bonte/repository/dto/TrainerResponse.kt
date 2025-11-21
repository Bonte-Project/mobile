package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainerResponse(
    val message: String,
    val trainer: TrainerDto
)
