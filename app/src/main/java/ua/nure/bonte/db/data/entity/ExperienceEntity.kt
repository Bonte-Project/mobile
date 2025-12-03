package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExperienceEntity(
    @PrimaryKey val experienceId: String,
    val trainerId: String,
    val title: String,
    val description: String?,
    val startDate: String?,
    val endDate: String?,
)
