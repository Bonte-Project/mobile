package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.SessionEntity
import java.time.LocalDate

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<SessionEntity>)

    @Transaction
    suspend fun syncInsertForUser(list: List<SessionEntity>) {
        val newIds = list.map { it.id }
        deleteNotInForUser(userId = list.first().userId, newIds)
        insert(list)

    }

    @Transaction
    suspend fun syncInsertForTrainer(list: List<SessionEntity>) {
        val newIds = list.map { it.id }
        deleteNotInForTrainer(trainerId = list.first().trainerId, newIds)
        insert(list)

    }

    @Query("DELETE FROM SessionEntity WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM SessionEntity WHERE trainerId=:trainerId AND scheduledAt BETWEEN :dayStartMils AND :dayEndMils ")
    fun getSessionsByDay(trainerId: String, dayStartMils: Long, dayEndMils: Long): Flow<List<SessionEntity>>

    @Query("DELETE FROM SessionEntity WHERE userId = :userId AND id NOT IN(:ids)")
    suspend fun deleteNotInForUser(userId: String, ids: List<String>)

    @Query("DELETE FROM SessionEntity WHERE trainerId = :trainerId AND id NOT IN(:ids)")
    suspend fun deleteNotInForTrainer(trainerId: String, ids: List<String>)
}