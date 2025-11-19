package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["trainerId", "experienceId"])
data class TrainerToExperienceEntity(
    val trainerId: String,
    val experienceId: String
)
