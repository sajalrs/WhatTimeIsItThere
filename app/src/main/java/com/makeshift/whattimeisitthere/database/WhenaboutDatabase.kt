package com.makeshift.whattimeisitthere.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makeshift.whattimeisitthere.Whenabout

@Database(entities = [Whenabout::class], version = 2)
@TypeConverters(WhenaboutTypeConverters::class)
abstract class WhenaboutDatabase: RoomDatabase() {

    abstract fun whenaboutDao(): WhenaboutDao

}