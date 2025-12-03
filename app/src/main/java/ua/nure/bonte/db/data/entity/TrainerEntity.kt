package ua.nure.bonte.db.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class TrainerEntity(
    @PrimaryKey val trainerId: String,
    val userId: String,
    val bio: String,
    val certification: String,
    val specialization: String,
    val location: String?,
    val isActive: Boolean,
)

data class Trainer(
    @Embedded val trainerEntity: TrainerEntity,
    @Relation(
        parentColumn = "trainerId",
        entityColumn = "experienceId",
        associateBy = Junction(TrainerToExperienceEntity::class)
    ) val experience: List<ExperienceEntity>?,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id",
        entity = ProfileEntity::class
    ) val profile: ProfileEntity?
)

