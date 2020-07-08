package com.makeshift.whattimeisitthere

import androidx.lifecycle.ViewModel

class WhenaboutListViewModel: ViewModel() {

    private val whenaboutRepository = WhenaboutRepository.get()
    val whenaboutsListLiveData = whenaboutRepository.getWhenabouts()

    fun addWhenabout(whenabout: Whenabout){
        whenaboutRepository.addWheabout(whenabout)
    }
}