package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrainerDto(
    val id: String,
    val userId: String,
    val bio: String?,
    val certification: String?,
    val specialization: String?,
    val location: String? = null,
    val isActive: Boolean,
    val experience: List<ExperienceDto> = emptyList(),
    val createdAt: String
)
