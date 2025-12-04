package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.SleepLogEntity

@Dao
interface SleepLogDao {

    @Query("SELECT * FROM sleep_logs")
    fun getAllSleepLogs(): Flow<List<SleepLogEntity>>

    @Query("SELECT * FROM sleep_logs WHERE id = :id LIMIT 1")
    suspend fun getSleepLogById(id: String): SleepLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleepLog(log: SleepLogEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SleepLogEntity>)


    @Update
    suspend fun updateSleepLog(log: SleepLogEntity)

    @Delete
    suspend fun deleteSleepLog(log: SleepLogEntity)

    @Query("DELETE FROM sleep_logs WHERE id = :id")
    suspend fun deleteById(id: String)
}
