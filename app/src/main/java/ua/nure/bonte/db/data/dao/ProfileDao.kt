package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.Profile
import ua.nure.bonte.db.data.entity.ProfileEntity

@Dao
interface ProfileDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ProfileEntity)

    @Query("SELECT * FROM profileentity LIMIT 1")
    fun getProfile(): Flow<Profile>

    @Query("SELECT * FROM profileentity LIMIT 1")
    fun getProfileEntity(): ProfileEntity?
}