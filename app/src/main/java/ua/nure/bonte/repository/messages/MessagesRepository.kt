package ua.nure.bonte.repository.messages

import kotlinx.coroutines.flow.Flow
import ua.nure.bonte.db.data.entity.MessageEntity
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.Result
import ua.nure.bonte.repository.dto.ChatPartnerListResponse
import ua.nure.bonte.repository.dto.MessageDto

interface MessagesRepository {

    suspend fun getChatList(): List<String>

    suspend fun loadChatList(): Result<ChatPartnerListResponse, DataError>

    suspend fun getChatHistory(conversationId: String): List<MessageDto>

    fun observeChatHistory(conversationId: String): Flow<List<MessageEntity>>

    suspend fun sendMessage(
        conversationId: String,
        message: String,
        toTrainer: Boolean
    ): MessageDto

    fun observeIncomingMessages(conversationId: String): Flow<MessageDto?>
}
