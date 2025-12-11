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

    @Query("SELECT * FROM profileentity WHERE isOwned == true")
    fun getProfile(): Flow<Profile>

    @Query("SELECT * FROM profileentity WHERE isOwned == true")
    fun getProfileEntity(): ProfileEntity?

    @Query("SELECT * FROM ProfileEntity WHERE id IN (:list)")
    fun getUsersFromList(list: List<String>): Flow<List<ProfileEntity>>
}