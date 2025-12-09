package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val userId: String,
    val trainerId: String,
    val message: String,
    val fromTrainer: Boolean,
    val createdAt: String
)

