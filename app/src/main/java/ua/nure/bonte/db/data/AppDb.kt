package ua.nure.bonte.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.nure.bonte.db.data.dao.ExperienceDao
import ua.nure.bonte.db.data.dao.ProfileDao
import ua.nure.bonte.db.data.dao.TrainerDao
import ua.nure.bonte.db.data.dao.TrainerToExperienceDao
import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.db.data.entity.TrainerToExperienceEntity


@Database(
    entities = [
        ProfileEntity::class,
        TrainerEntity::class,
        ExperienceEntity::class,
        TrainerToExperienceEntity::class,
    ],
    version = 2
)
@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract val profileDao: ProfileDao
    abstract val trainerDao: TrainerDao
    abstract val experienceDao: ExperienceDao
    abstract val trainerToExperienceDao: TrainerToExperienceDao
}