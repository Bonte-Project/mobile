package ua.nure.bonte.repository.dto

data class ActivityLogResponse(
    val message: String,
    val logs: List<ActivityLogDto>
)
