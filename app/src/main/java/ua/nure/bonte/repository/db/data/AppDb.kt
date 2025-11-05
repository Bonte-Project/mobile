package ua.nure.bonte.repository.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.nure.bonte.repository.db.data.dao.ProfileDao
import ua.nure.bonte.repository.db.data.entity.ProfileEntity


@Database(
    entities = [
        ProfileEntity::class
    ],
    version = 1
)
@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract val profileDao: ProfileDao
}