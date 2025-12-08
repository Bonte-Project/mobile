package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class SleepLogResponse(
    val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val quality: Int
)

@Serializable
data class SleepLogsResponse(
    val message: String,
    val logs: List<SleepLogResponse>
)

@Serializable
data class SleepLogSingleResponse(
    val log: SleepLogResponse
)
