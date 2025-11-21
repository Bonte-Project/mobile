package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainerDto(
    val id: String,
    val userId: String,
    val bio: String,
    val certification: String,
    val specialization: String,
    val location: String,
    val isActive: Boolean,
    val experience: List<ExperienceDto> = emptyList()
)
