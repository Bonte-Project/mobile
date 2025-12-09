package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.Trainer
import ua.nure.bonte.db.data.entity.TrainerEntity

@Dao
interface TrainerDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<TrainerEntity>)

    @Query("SELECT * FROM TrainerEntity WHERE userId = :userId")
    fun getTrainerByUserId(userId: String): Flow<TrainerEntity>

    @Query("SELECT * FROM TrainerEntity WHERE trainerId = :trainerId")
    fun getTrainerById(trainerId: String): Flow<Trainer>

    @Query("SELECT * FROM TrainerEntity")
    fun getTrainerList(): Flow<List<Trainer>>

}