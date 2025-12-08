package ua.nure.bonte.db.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.NutritionLogEntity

@Dao
interface NutritionLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: NutritionLogEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<NutritionLogEntity>)

    @Query("SELECT * FROM NutritionLogEntity WHERE eatenAt >= :startDate AND eatenAt < :endDate")
    fun getLogsForPeriod(startDate: String, endDate: String): Flow<List<NutritionLogEntity>>
    @Query("SELECT * FROM NutritionLogEntity")
    fun getAllLogs(): Flow<List<NutritionLogEntity>>

    @Query("SELECT * FROM NutritionLogEntity WHERE id = :id")
    suspend fun getLogById(id: String): NutritionLogEntity?

    @Delete
    suspend fun delete(log: NutritionLogEntity)

    @Query("DELETE FROM NutritionLogEntity WHERE id = :id")
    suspend fun deleteById(id: String)
}