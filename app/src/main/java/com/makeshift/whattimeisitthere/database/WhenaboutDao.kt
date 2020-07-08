package com.makeshift.whattimeisitthere.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.makeshift.whattimeisitthere.Whenabout
import java.util.*

@Dao
interface WhenaboutDao {

    @Query("SELECT * FROM whenabout")
    fun getWhenabouts(): LiveData<List<Whenabout>>

    @Query("SELECT * FROM whenabout WHERE id=(:id)")
    fun getWhenabout(id: UUID): LiveData<Whenabout?>

    @Update
    fun UpdateWhenabout(whenabout: Whenabout)

    @Insert
    fun addWhenabout(whenabout: Whenabout)

}