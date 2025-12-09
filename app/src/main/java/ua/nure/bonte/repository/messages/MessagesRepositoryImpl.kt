package ua.nure.bonte.repository.messages

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import ua.nure.bonte.db.data.AppDb
import ua.nure.bonte.db.data.entity.MessageEntity
import ua.nure.bonte.repository.dto.ChatPartnerListResponse
import ua.nure.bonte.repository.dto.MessageDto
import ua.nure.bonte.repository.dto.MessageSingleResponse
import ua.nure.bonte.repository.dto.MessagesListResponse
import ua.nure.bonte.repository.dto.SendMessageRequest
import ua.nure.bonte.repository.dto.mapper.MessageMapper
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val appDb: AppDb,
    private val dbDispatcher: CoroutineDispatcher
) : MessagesRepository {

    override suspend fun getChatList(): List<String> {
        val response: ChatPartnerListResponse =
            httpClient.get("trainer-messages/chats/list").body()

        return response.data
    }

    override suspend fun getChatHistory(conversationId: String): List<MessageDto> {
        val response: MessagesListResponse =
            httpClient.get("trainer-messages/$conversationId").body()
        val sorted = response.data.sortedBy { it.sent_at }
        val entities = sorted.map { dto ->
            MessageMapper.dtoToEntity(dto, conversationId)
        }
        withContext(dbDispatcher) {
            appDb.messageDao.insertMessages(entities)
        }

        return sorted
    }

    override fun observeChatHistory(conversationId: String): Flow<List<MessageEntity>> =
        appDb.messageDao.getMessages(conversationId)
            .onEach { list ->
                println(">>> DAO OBSERVE conversationId=$conversationId, listSize=${list.size}")
                list.forEach { println(">>> DAO MSG: id=${it.id}, fromTrainer=${it.fromTrainer}") }
            }
    override suspend fun sendMessage(
        conversationId: String,
        message: String,
        toTrainer: Boolean
    ): MessageDto {

        val body = SendMessageRequest(
            message = message,
            to_from = toTrainer
        )

        val response: MessageSingleResponse =
            httpClient.post("trainer-messages/$conversationId") {
                setBody(body)
            }.body()

        val created = response.data

        withContext(dbDispatcher) {
            appDb.messageDao.insertMessage(
                MessageMapper.dtoToEntity(created, conversationId)
            )
        }
        return created
    }

    override fun observeIncomingMessages(conversationId: String): Flow<MessageDto?> = flow {
        while (true) {
            try {
                val msg: MessageDto? = httpClient.get("trainer-messages/poll/new").body()

                msg?.let {
                    val entity = MessageMapper.dtoToEntity(it, conversationId)
                    withContext(dbDispatcher) {
                        appDb.messageDao.insertMessage(entity)
                    }
                }

                emit(msg)
            } catch (_: Exception) {
                emit(null)
            }
        }
    }

}
