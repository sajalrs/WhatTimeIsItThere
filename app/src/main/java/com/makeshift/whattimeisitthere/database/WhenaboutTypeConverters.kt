package com.makeshift.whattimeisitthere.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
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

    @TypeConverter
    fun fromDate(date: Date): String{
        val datePattern = SimpleDateFormat("MM/dd/YYYY")
        return datePattern.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String?): Date?{
        val datePattern = SimpleDateFormat("MM/dd/YYYY")
        return datePattern.parse(dateString)
    }
}