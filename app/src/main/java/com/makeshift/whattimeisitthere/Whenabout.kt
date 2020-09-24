package com.makeshift.whattimeisitthere

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*
@Entity
data class Whenabout(@PrimaryKey val id: UUID = UUID.randomUUID(),
                     var name: String = "",
                     var timeZone: TimeZone = TimeZone.getDefault(),
                     var dob: Date = Date()): Serializable {

    val photoFileName
        get() = "IMG_$id.jpg"
}