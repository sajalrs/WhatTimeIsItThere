package com.makeshift.whattimeisitthere

import androidx.lifecycle.ViewModel
import java.io.File

class WhenaboutListViewModel: ViewModel() {

    private val whenaboutRepository = WhenaboutRepository.get()
    val whenaboutsListLiveData = whenaboutRepository.getWhenabouts()
    var lastEdited = -1


    fun addWhenabout(whenabout: Whenabout){
        whenaboutRepository.addWheabout(whenabout)
    }

    fun saveWhenabout(whenabout: Whenabout){
        whenaboutRepository.updateWhenabout(whenabout)
    }

    fun deleteWhenabout(whenabout: Whenabout){
        whenaboutRepository.deleteWhenabout(whenabout)
    }

    fun getPhotoFile(whenabout: Whenabout): File {
        return whenaboutRepository.getPhotoFile(whenabout)
    }
}