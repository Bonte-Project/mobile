package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.NutritionGoalEntity
import ua.nure.bonte.db.data.entity.NutritionLogEntity
import ua.nure.bonte.repository.dto.NutritionGoalDto
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.repository.dto.NutritionLogDto

fun NutritionLogDto.toEntity(): NutritionLogEntity = NutritionLogEntity(
    id = id,
    userId = userId,
    eatenAt = eatenAt,
    mealType = mealType,
    name = name,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    weightInGrams = weightInGrams,
    createdAt = createdAt
)

fun NutritionLogEntity.toDto(): NutritionLogDto = NutritionLogDto(
    id = id,
    userId = userId,
    eatenAt = eatenAt,
    mealType = mealType,
    name = name,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    weightInGrams = weightInGrams,
    createdAt = createdAt
)

fun NutritionGoalRequest.toEntity(userId: String = "dummy"): NutritionGoalEntity {
    return NutritionGoalEntity(
        id = 0,
        userId = userId,
        calories = this.calories,
        protein = this.protein,
        carbs = this.carbs,
        fat = this.fat,
        createdAt = "",
        updatedAt = ""
    )
}

fun NutritionGoalEntity.toResponseDto(): NutritionGoalRequest = NutritionGoalRequest(
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat
)

fun NutritionGoalDto.toEntity(): NutritionGoalEntity = NutritionGoalEntity(
    id = id,
    userId = userId,
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun NutritionGoalDto.toRequest(): NutritionGoalRequest = NutritionGoalRequest(
    calories = calories,
    protein = protein,
    carbs = carbs,
    fat = fat
)
