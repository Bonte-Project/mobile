package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutritionGoalEntity(
    @PrimaryKey val id: Int,
    val userId: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val createdAt: String,
    val updatedAt: String
)

