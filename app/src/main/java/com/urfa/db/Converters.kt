package com.urfa.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimeStamp(value: Long?): Calendar? {
        return if (value == null) null else {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = value
            calendar
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Calendar?): Long? {
        return date?.timeInMillis
    }
}