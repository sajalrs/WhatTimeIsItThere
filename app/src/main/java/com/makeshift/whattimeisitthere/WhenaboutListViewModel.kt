package com.makeshift.whattimeisitthere

import androidx.lifecycle.ViewModel
import java.util.*

class WhenaboutListViewModel: ViewModel() {

    private val whenaboutRepository = WhenaboutRepository.get()
    val whenaboutsListLiveData = whenaboutRepository.getWhenabouts()
    var curEditable = UUID(0,0)

//    init {
//        val toAdd1 = Whenabout(UUID.randomUUID(),"Sajal\nSatyal", TimeZone.getDefault())
//        val toAdd2 = Whenabout(UUID.randomUUID(),"Sagun\nSatyal", TimeZone.getDefault())
//        addWhenabout(toAdd1)
//        addWhenabout(toAdd2)
//    }

    fun addWhenabout(whenabout: Whenabout){
        whenaboutRepository.addWheabout(whenabout)
    }

    fun saveWhenabout(whenabout: Whenabout){
        whenaboutRepository.updateWhenabout(whenabout)
    }
}