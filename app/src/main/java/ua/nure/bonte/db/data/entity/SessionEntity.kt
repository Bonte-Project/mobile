package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.nure.bonte.repository.dto.SessionStatus
import java.time.LocalDateTime

@Entity
data class SessionEntity (
    @PrimaryKey val id: String,
    val name: String,
    val userId: String,
    val trainerId: String,
    val scheduledAt: LocalDateTime,
    val status: SessionStatus,
)