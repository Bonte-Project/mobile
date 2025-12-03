package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_logs")
data class SleepLogEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val startTime: String,
    val endTime: String,
    val quality: Int
)
