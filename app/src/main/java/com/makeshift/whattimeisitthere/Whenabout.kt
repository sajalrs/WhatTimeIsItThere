package com.makeshift.whattimeisitthere

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Whenabout(@PrimaryKey val id: UUID = UUID.randomUUID(),
                     val name: String = "",
                     val timeZone: TimeZone = TimeZone.getDefault()) {
}