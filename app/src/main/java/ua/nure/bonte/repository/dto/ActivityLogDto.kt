package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityLogDto(
    val id: String,
    val userId: String,
    val activityType: String,
    val intensity: String,
    val durationMinutes: Int,
    val completedAt: String,
)
