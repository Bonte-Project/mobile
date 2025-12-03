package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.SleepLogEntity
import ua.nure.bonte.repository.dto.SleepLogDto

fun SleepLogDto.toEntity(): SleepLogEntity =
    SleepLogEntity(
        id = id,
        userId = userId,
        startTime = startTime,
        endTime = endTime,
        quality = quality
    )

fun SleepLogEntity.toDto(): SleepLogDto =
    SleepLogDto(
        id = id,
        userId = userId,
        startTime = startTime,
        endTime = endTime,
        quality = quality
    )
