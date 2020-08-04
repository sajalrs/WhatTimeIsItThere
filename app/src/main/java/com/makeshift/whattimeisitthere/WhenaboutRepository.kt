package com.makeshift.whattimeisitthere

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.makeshift.whattimeisitthere.database.WhenaboutDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "whenabouts-database"
class WhenaboutRepository private constructor(context: Context){

    val MIGRATION_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Whenabout ADD COLUMN dob INTEGER NOT NULL DEFAULT 0")
        }

    }

    private val database: WhenaboutDatabase = Room.databaseBuilder(
        context.applicationContext,
        WhenaboutDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(MIGRATION_1_2).build()


    private val whenaboutDao = database.whenaboutDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getWhenabouts():LiveData<List<Whenabout>> = whenaboutDao.getWhenabouts()

    fun getWhenabouts(id: UUID): LiveData<Whenabout?> = whenaboutDao.getWhenabout(id)

    fun getBirthdayWhenabouts(dob: Date): LiveData<List<Whenabout>> = whenaboutDao.getBirthdayWhenabouts(dob)

    fun updateWhenabout(whenabout: Whenabout){
        executor.execute{
            whenaboutDao.UpdateWhenabout(whenabout)
        }
    }

    fun addWheabout(whenabout: Whenabout){
        executor.execute{
            whenaboutDao.addWhenabout(whenabout)
        }
    }

    companion object{
        private var INSTANCE: WhenaboutRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = WhenaboutRepository(context)
            }
        }

        fun get():WhenaboutRepository{
            return INSTANCE?:
                    throw IllegalStateException("WhenaboutRepository must be initialized")
        }
    }
}