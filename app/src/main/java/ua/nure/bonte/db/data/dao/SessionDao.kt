package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ua.nure.bonte.db.data.entity.SessionEntity

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<SessionEntity>)

    @Query("DELETE FROM SessionEntity WHERE id = :id")
    suspend fun deleteById(id: String)
}