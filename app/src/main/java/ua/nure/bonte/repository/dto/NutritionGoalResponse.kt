package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutritionGoalResponse(
    val message: String,
    val goal: NutritionGoalDto
)

