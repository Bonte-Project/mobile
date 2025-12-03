package ua.nure.bonte.repository.dto.mapper

import ua.nure.bonte.db.data.entity.ActivityLogEntity
import ua.nure.bonte.repository.dto.ActivityLogDto
import ua.nure.bonte.repository.dto.ActivityLogResponse

fun ActivityLogEntity.toDto(): ActivityLogDto = ActivityLogDto(
    id = this.id,
    userId = this.userId,
    activityType = this.activityType,
    intensity = this.intensity,
    durationMinutes = this.durationMinutes,
    completedAt = this.completedAt
)

fun ActivityLogDto.toEntity(): ActivityLogEntity = ActivityLogEntity(
    id = this.id,
    userId = this.userId,
    activityType = this.activityType,
    intensity = this.intensity,
    durationMinutes = this.durationMinutes,
    completedAt = this.completedAt
)
fun ActivityLogResponse.toEntity(): ActivityLogEntity = ActivityLogEntity(
    id = this.logs.first().id,
    userId = this.logs.first().userId,
    activityType = this.logs.first().activityType,
    intensity = this.logs.first().intensity,
    durationMinutes = this.logs.first().durationMinutes,
    completedAt = this.logs.first().completedAt
)

fun ActivityLogResponse.toDto(): ActivityLogDto = ActivityLogDto(
    id = this.logs.first().id,
    userId = this.logs.first().userId,
    activityType = this.logs.first().activityType,
    intensity = this.logs.first().intensity,
    durationMinutes = this.logs.first().durationMinutes,
    completedAt = this.logs.first().completedAt
)