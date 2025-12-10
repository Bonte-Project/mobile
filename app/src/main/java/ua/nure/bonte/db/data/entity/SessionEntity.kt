package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.nure.bonte.repository.dto.SessionStatus
import java.time.LocalDateTime

@Entity
data class SessionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val userId: String,
    val trainerId: String,
    val scheduledAt: LocalDateTime,
    val status: SessionStatus,
) {
    companion object {
        val preview = listOf<SessionEntity>(
            SessionEntity(
                id = "1",
                name = "Session 1",
                userId = "",
                trainerId = "",
                scheduledAt = LocalDateTime.now(),
                status = SessionStatus.scheduled
            ),
            SessionEntity(
                id = "2",
                name = "Session 2",
                userId = "",
                trainerId = "",
                scheduledAt = LocalDateTime.now(),
                status = SessionStatus.cancelled
            ),
            SessionEntity(
                id = "3",
                name = "Session 3",
                userId = "",
                trainerId = "",
                scheduledAt = LocalDateTime.now(),
                status = SessionStatus.completed
            )
        )
    }
}