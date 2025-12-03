package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSleepLogDto(
    val startTime: String? = null,
    val endTime: String? = null,
    val quality: Int? = null
)
