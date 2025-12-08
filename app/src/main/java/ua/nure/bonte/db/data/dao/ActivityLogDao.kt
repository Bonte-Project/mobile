package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.ActivityLogEntity

@Dao
interface ActivityLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ActivityLogEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ActivityLogEntity>)


    @Query("SELECT * FROM ActivityLogEntity")
    fun getAllLogs(): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM ActivityLogEntity WHERE id = :id")
    suspend fun getLogById(id: String): ActivityLogEntity?

    @Delete
    suspend fun delete(log: ActivityLogEntity)

    @Query("DELETE FROM ActivityLogEntity WHERE id = :id")
    suspend fun deleteById(id: String)
    @Update
    suspend fun updateActivityLog(entity: ActivityLogEntity)

    @Query("SELECT * FROM ActivityLogEntity WHERE completedAt >= :startDate AND completedAt < :endDate")
    fun getLogsForPeriod(startDate: String, endDate: String): Flow<List<ActivityLogEntity>>
}