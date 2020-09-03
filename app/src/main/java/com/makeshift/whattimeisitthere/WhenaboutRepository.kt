package com.makeshift.whattimeisitthere

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.makeshift.whattimeisitthere.database.WhenaboutDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "whenabouts-database"
class WhenaboutRepository private constructor(context: Context){

    private val database: WhenaboutDatabase = Room.databaseBuilder(
        context.applicationContext,
        WhenaboutDatabase::class.java,
        DATABASE_NAME
    ).build()


    private val whenaboutDao = database.whenaboutDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getWhenabouts():LiveData<List<Whenabout>> = whenaboutDao.getWhenabouts()

    fun getWhenabouts(id: UUID): LiveData<Whenabout?> = whenaboutDao.getWhenabout(id)

    fun getBirthdayWhenabouts(dob: Date): List<Whenabout> = whenaboutDao.getBirthdayWhenabouts(dob)

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

    fun deleteWhenabout(whenabout: Whenabout){
        executor.execute{
            whenaboutDao.removeWhenabout(whenabout)
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