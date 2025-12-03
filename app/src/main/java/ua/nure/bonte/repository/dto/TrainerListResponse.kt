package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainerListResponse(
    val message: String,
    val trainers: List<TrainerDto>
)
