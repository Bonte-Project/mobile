package ua.nure.bonte.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.nure.bonte.db.data.entity.AiMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AiMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AiMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<AiMessageEntity>)

    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId ORDER BY `index` ASC")
    fun getMessagesForConversation(conversationId: String): Flow<List<AiMessageEntity>>

    @Query("SELECT * FROM ai_messages WHERE conversationId = :conversationId AND is_system = 0 ORDER BY `index` ASC")
    fun getVisibleMessagesForConversation(conversationId: String): Flow<List<AiMessageEntity>>

    @Query("DELETE FROM ai_messages WHERE conversationId = :conversationId")
    suspend fun clearConversation(conversationId: String)
}
