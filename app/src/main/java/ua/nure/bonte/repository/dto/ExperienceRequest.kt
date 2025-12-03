package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceRequest(
    val title: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)

@Serializable
data class ExperienceDto(
    val id: String,
    val trainerId: String,
    val title: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null
)
