package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutritionLogResponse(
    val message: String,
    val logs: List<NutritionLogDto>
)
