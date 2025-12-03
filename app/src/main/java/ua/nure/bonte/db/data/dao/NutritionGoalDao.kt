package ua.nure.bonte.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.NutritionGoalEntity

@Dao
interface NutritionGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: NutritionGoalEntity)

    @Query("SELECT * FROM NutritionGoalEntity WHERE userId = :userId")
    fun getGoal(userId: String): Flow<NutritionGoalEntity?>

    @Query("DELETE FROM NutritionGoalEntity WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)

    @Query("DELETE FROM NutritionGoalEntity")
    suspend fun clearAll()

}