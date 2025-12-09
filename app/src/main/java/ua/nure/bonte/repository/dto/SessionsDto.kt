package ua.nure.bonte.repository.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class SessionsDto(
    val message: String,
    val sessions: List<SessionDto>
)
@Serializable
data class CreateSessionDto(
    val message: String,
    val session: SessionDto
)

@Serializable
data class SessionDto(
    val id: String,
    val name: String,
    val userId: String,
    val trainerId: String,
    @Serializable(with = LocalDateTimeSerializer::class) val scheduledAt: LocalDateTime,
    val status: SessionStatus,
)

@Serializable(with = SessionStatusSerializer::class)
enum class SessionStatus {
    scheduled, completed, cancelled
}

object SessionStatusSerializer : KSerializer<SessionStatus> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("SessionStatus", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: SessionStatus
    ) {
        encoder.encodeString(
            when (value) {
                SessionStatus.scheduled -> "scheduled"
                SessionStatus.completed -> "completed"
                SessionStatus.cancelled -> "cancelled"
            }
        )
    }

    override fun deserialize(decoder: Decoder): SessionStatus =
        when (decoder.decodeString()) {
            "scheduled" -> SessionStatus.scheduled
            "completed" -> SessionStatus.completed
            "cancelled" -> SessionStatus.cancelled
            else -> throw IllegalArgumentException("Invalid value")
        }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}