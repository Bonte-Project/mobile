package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_conversations")
data class AiConversationEntity(
    @PrimaryKey val id: String,
    val createdAt: String,
    val initialized: Boolean = false,
    val systemPromptId: String? = null
)
