package com.makeshift.whattimeisitthere

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Whenabout(@PrimaryKey val id: UUID = UUID.randomUUID(),
                     var name: String = "",
                     var timeZone: TimeZone = TimeZone.getDefault(),
                     var dob: Date = Date().apply {
                         var calendar = Calendar.getInstance()
                         calendar.set(Calendar.YEAR + 1900, Calendar.MONTH, Calendar.DATE)
                         this.time = calendar.timeInMillis
                     }) {
}