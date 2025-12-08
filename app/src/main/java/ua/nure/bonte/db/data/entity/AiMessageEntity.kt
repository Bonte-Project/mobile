package ua.nure.bonte.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "ai_messages")
data class AiMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val message: String,
    val sentAt: String,
    @ColumnInfo(name = "to_from") val toFrom: Boolean, // true = user, false = ai
    val sender: String? = null,
    val index: Int,
    @ColumnInfo(name = "is_system") val isSystem: Boolean = false
)
