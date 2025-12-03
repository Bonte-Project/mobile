package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ActivityLogEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val activityType: String,
    val intensity: String,
    val durationMinutes: Int,
    val completedAt: String,
)
