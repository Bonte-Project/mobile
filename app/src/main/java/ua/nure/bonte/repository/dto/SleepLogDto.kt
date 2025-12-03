package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SleepLogDto(
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val quality: Int
)
