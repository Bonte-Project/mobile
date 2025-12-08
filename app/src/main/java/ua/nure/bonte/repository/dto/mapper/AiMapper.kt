package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.AiConversationEntity
import ua.nure.bonte.db.data.entity.AiMessageEntity
import ua.nure.bonte.repository.dto.AiConversationDto
import ua.nure.bonte.repository.dto.AiMessageDto

object AiMapper {
    fun dtoToConversationEntity(dto: AiConversationDto): AiConversationEntity =
        AiConversationEntity(
            id = dto.id,
            createdAt = dto.createdAt,
            systemPromptId = dto.systemPromptId,
            initialized = true
        )

    fun dtoToMessageEntity(dto: AiMessageDto, conversationId: String): AiMessageEntity =
        AiMessageEntity(
            id = dto.id,
            conversationId = conversationId,
            message = dto.message,
            sentAt = dto.sentAt,
            toFrom = dto.toFrom,
            sender = dto.sender,
            index = dto.index,
            isSystem = dto.isSystem
        )

    fun messageEntityToDto(entity: AiMessageEntity): AiMessageDto =
        AiMessageDto(
            id = entity.id,
            message = entity.message,
            sentAt = entity.sentAt,
            toFrom = entity.toFrom,
            sender = entity.sender,
            index = entity.index,
            isSystem = entity.isSystem
        )

    fun conversationEntityToDto(entity: AiConversationEntity): AiConversationDto =
        AiConversationDto(
            id = entity.id,
            createdAt = entity.createdAt,
            systemPromptId = entity.systemPromptId
        )
}