package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.MessageEntity
import ua.nure.bonte.repository.dto.MessageDto

object MessageMapper {

    fun dtoToEntity(dto: MessageDto, conversationId: String): MessageEntity {
        return MessageEntity(
            id = dto.id,
            conversationId = conversationId,
            userId = dto.user_id,
            trainerId = dto.trainer_id,
            message = dto.message,
            fromTrainer = dto.to_from,
            createdAt = dto.sent_at
        )
    }

    fun entityToDto(entity: MessageEntity): MessageDto {
        return MessageDto(
            id = entity.id,
            user_id = entity.userId,
            trainer_id = entity.trainerId,
            message = entity.message,
            sent_at = entity.createdAt,
            to_from = !entity.fromTrainer
        )
    }

}
