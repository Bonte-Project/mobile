package ua.nure.bonte.repository.ai

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.AiConversationEntity
import ua.nure.bonte.db.data.entity.AiMessageEntity
import ua.nure.bonte.repository.dto.CreateChatResponseDto
import ua.nure.bonte.repository.dto.GetHistoryResponseDto
import ua.nure.bonte.repository.dto.SendMessageResponseDto

interface AIRepository {
    suspend fun createChat(): CreateChatResponseDto
    suspend fun getChatHistory(conversationId: String): GetHistoryResponseDto
    fun observeChatHistory(conversationId: String): Flow<List<AiMessageEntity>>
    fun observeVisibleHistory(conversationId: String): Flow<List<AiMessageEntity>>
    suspend fun getVisibleHistoryOnce(conversationId: String): GetHistoryResponseDto
    suspend fun sendMessage(conversationId: String, message: String): SendMessageResponseDto
    suspend fun getOrCreateLocalConversation(conversationId: String): AiConversationEntity?
}
