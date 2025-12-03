package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSleepLogDto(
    val startTime: String,
    val endTime: String,
    val quality: Int
)
