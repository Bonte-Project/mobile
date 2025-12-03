package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutritionGoalRequest(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)