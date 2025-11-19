package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ua.nure.bonte.db.data.entity.TrainerToExperienceEntity

@Dao
interface TrainerToExperienceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<TrainerToExperienceEntity>)
}