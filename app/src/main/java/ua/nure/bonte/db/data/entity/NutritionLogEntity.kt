package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutritionLogEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val eatenAt: String,
    val mealType: String,
    val name: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val weightInGrams: Int,
    val createdAt: String
)

