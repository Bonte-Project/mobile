package ua.nure.bonte.repository.db.data

import androidx.room.TypeConverter
import java.util.Date

class DbConverters {
    @TypeConverter
    fun dataToDb(date: Date): Long = date.time

    @TypeConverter
    fun dateFromDb(time: Long): Date = Date(time)

}