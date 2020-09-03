package com.makeshift.whattimeisitthere.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.makeshift.whattimeisitthere.Whenabout
import java.util.*

@Dao
interface WhenaboutDao {

    @Query("SELECT * FROM whenabout")
    fun getWhenabouts(): LiveData<List<Whenabout>>

    @Query("SELECT * FROM whenabout WHERE id=(:id)")
    fun getWhenabout(id: UUID): LiveData<Whenabout?>

    @Query("SELECT * FROM whenabout WHERE substr(dob,1,5) = substr((:dob),1,5)")
    fun getBirthdayWhenabouts(dob: Date): List<Whenabout>

    @Update
    fun UpdateWhenabout(whenabout: Whenabout)

    @Insert
    fun addWhenabout(whenabout: Whenabout)

    @Delete
    fun removeWhenabout(whenabout: Whenabout)
}