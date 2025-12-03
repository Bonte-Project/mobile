package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable
@Serializable
data class NutritionGoalDto(
    val id: Int,
    val userId: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val createdAt: String,
    val updatedAt: String
)