package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.dto.SessionDto
import ua.nure.bonte.repository.dto.SessionsDto

fun SessionDto.toEntity() =
    SessionEntity(
        id = id,
        name = name,
        userId = userId,
        trainerId = trainerId,
        scheduledAt = scheduledAt,
        status = status
    )