package ua.nure.bonte.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.nure.bonte.db.dao.NutritionGoalDao
import ua.nure.bonte.db.data.dao.ExperienceDao
import ua.nure.bonte.db.data.dao.ProfileDao
import ua.nure.bonte.db.data.dao.TrainerDao
import ua.nure.bonte.db.data.dao.NutritionLogDao
import ua.nure.bonte.db.data.dao.ActivityLogDao
import ua.nure.bonte.db.data.dao.TrainerToExperienceDao
import ua.nure.bonte.db.data.dao.SleepLogDao
import ua.nure.bonte.db.data.entity.ActivityLogEntity
import ua.nure.bonte.db.data.entity.ExperienceEntity
import ua.nure.bonte.db.data.entity.NutritionGoalEntity
import ua.nure.bonte.db.data.entity.NutritionLogEntity
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.TrainerEntity
import ua.nure.bonte.db.data.entity.TrainerToExperienceEntity
import ua.nure.bonte.db.data.entity.SleepLogEntity

@Database(
    entities = [
        ProfileEntity::class,
        TrainerEntity::class,
        ExperienceEntity::class,
        TrainerToExperienceEntity::class,
        SleepLogEntity::class,
        NutritionLogEntity::class,
        NutritionGoalEntity::class,
        ActivityLogEntity::class,
    ],
    version = 5
)
@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract val profileDao: ProfileDao
    abstract val trainerDao: TrainerDao
    abstract val experienceDao: ExperienceDao
    abstract val trainerToExperienceDao: TrainerToExperienceDao
    abstract val sleepLogDao: SleepLogDao
    abstract val nutritionLogDao: NutritionLogDao
    abstract val nutritionGoalDao: NutritionGoalDao
    abstract val activityLogDao: ActivityLogDao
}