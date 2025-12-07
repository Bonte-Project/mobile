package ua.nure.bonte.repository.ai

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.db.data.entity.AiConversationEntity
import ua.nure.bonte.db.data.entity.AiMessageEntity
import ua.nure.bonte.di.ApiErrorException
import ua.nure.bonte.repository.dto.*
import ua.nure.bonte.repository.dto.mapper.AiMapper
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    private val dbDispatcher: CoroutineDispatcher
) : AIRepository {

    override suspend fun createChat(): CreateChatResponseDto = withContext(dbDispatcher) {
        val resp: CreateChatResponseDto = httpClient.post("ai/create-chat").body()
        resp.conversation?.let {
            appDb.aiConversationDao.insert(AiMapper.dtoToConversationEntity(it))
        }
        resp
    }

    override suspend fun getChatHistory(conversationId: String): GetHistoryResponseDto = withContext(dbDispatcher) {
        val resp: GetHistoryResponseDto = httpClient.get("ai/chat/history?conversationId=$conversationId").body()
        val entities = resp.messages.map { AiMapper.dtoToMessageEntity(it, conversationId) }
        appDb.aiMessageDao.insertMessages(entities)
        resp
    }

    override fun observeChatHistory(conversationId: String): Flow<List<AiMessageEntity>> =
        appDb.aiMessageDao.getMessagesForConversation(conversationId)

    override fun observeVisibleHistory(conversationId: String): Flow<List<AiMessageEntity>> =
        appDb.aiMessageDao.getVisibleMessagesForConversation(conversationId)

    override suspend fun getVisibleHistoryOnce(conversationId: String): GetHistoryResponseDto = withContext(dbDispatcher) {
        val resp: GetHistoryResponseDto = httpClient.get("ai/chat/visible-history?conversationId=$conversationId").body()
        val entities = resp.messages.map { AiMapper.dtoToMessageEntity(it, conversationId) }
        appDb.aiMessageDao.insertMessages(entities)
        resp
    }

    override suspend fun sendMessage(conversationId: String, message: String): SendMessageResponseDto =
        withContext(dbDispatcher) {
            val local = getOrCreateLocalConversation(conversationId)

            if (!local.initialized) {
                try {
                    val created = httpClient.post("ai/create-chat").body<CreateChatResponseDto>()
                    created.conversation?.let {
                        appDb.aiConversationDao.insert(AiMapper.dtoToConversationEntity(it))
                    }
                } catch (e: ApiErrorException) {
                    if (e.httpStatus == 409 && e.apiError.message.contains("active conversation")) {
                        val existing = httpClient.get("ai/chat/history?conversationId=$conversationId")
                            .body<GetHistoryResponseDto>()
                        val entities = existing.messages.map {
                            AiMapper.dtoToMessageEntity(it, conversationId)
                        }
                        appDb.aiMessageDao.insertMessages(entities)
                        appDb.aiConversationDao.insert(local.copy(initialized = true))
                    } else throw e
                }
            }

            val body = mapOf("conversationId" to conversationId, "message" to message)
            val resp = httpClient.post("ai/send-message") { setBody(body) }
                .body<SendMessageResponseDto>()

            resp.userMessage?.let {
                appDb.aiMessageDao.insertMessage(AiMapper.dtoToMessageEntity(it, conversationId))
            }
            resp.aiResponse?.let {
                appDb.aiMessageDao.insertMessage(AiMapper.dtoToMessageEntity(it, conversationId))
            }
            getChatHistory(conversationId)

            resp
        }

    override suspend fun getOrCreateLocalConversation(conversationId: String): AiConversationEntity = withContext(dbDispatcher) {
        val existing = appDb.aiConversationDao.getConversation(conversationId)
        if (existing != null) return@withContext existing

        val placeholder = AiConversationEntity(
            id = conversationId,
            createdAt = System.currentTimeMillis().toString(),
            initialized = false,
            systemPromptId = null
        )
        appDb.aiConversationDao.insert(placeholder)
        placeholder
    }
}
