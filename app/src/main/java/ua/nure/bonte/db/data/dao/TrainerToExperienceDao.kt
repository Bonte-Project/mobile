package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.nure.bonte.db.data.entity.TrainerToExperienceEntity

@Dao
interface TrainerToExperienceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<TrainerToExperienceEntity>)

    @Query("DELETE FROM TrainerToExperienceEntity WHERE experienceId = :id")
    suspend fun deleteByExperienceId(id: String)
}