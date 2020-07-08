package com.makeshift.whattimeisitthere.database

import androidx.room.TypeConverter
import java.util.*

class WhenaboutTypeConverters {

    @TypeConverter
    fun toUUID(uuid: String?): UUID?{
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String?{
        return uuid?.toString()
    }

    @TypeConverter
    fun toTimeZone(timeZoneString:String?): TimeZone?{
        return TimeZone.getTimeZone(timeZoneString)
    }

    @TypeConverter
    fun fromTimeZone(timeZone: TimeZone): String?{
        return timeZone?.id
    }
}