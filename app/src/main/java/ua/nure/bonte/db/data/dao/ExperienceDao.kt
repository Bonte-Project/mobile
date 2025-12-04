package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.ExperienceEntity

@Dao
interface ExperienceDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ExperienceEntity>)

    @Query("SELECT * FROM ExperienceEntity")
    fun getExperience(): Flow<List<ExperienceEntity>>

    @Query("DELETE FROM ExperienceEntity WHERE experienceId = :id")
    suspend fun deleteById(id: String)
}