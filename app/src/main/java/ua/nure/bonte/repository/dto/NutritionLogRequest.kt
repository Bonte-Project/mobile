package ua.nure.bonte.repository.dto

import kotlinx.serialization.Serializable

@Serializable
data class NutritionLogRequest(
    val eatenAt: String,
    val mealType: String,
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val weightInGrams: Int
)