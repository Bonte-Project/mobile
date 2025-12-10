package ua.nure.bonte.db.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class DbConverters {
    @TypeConverter
    fun localDateTimeToDb(value: LocalDateTime?): Long? {
        return value
            ?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }

    @TypeConverter
    fun localDateTimeFromDb(value: Long?): LocalDateTime? {
        return value
            ?.let { Instant.ofEpochMilli(it) }
            ?.atZone(ZoneId.systemDefault())
            ?.toLocalDateTime()
    }

}