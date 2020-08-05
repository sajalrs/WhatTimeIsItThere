package com.makeshift.whattimeisitthere

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

private const val TAG = "BirthdayWorker"
class BirthdayWorker(val context:Context, workerParams: WorkerParameters):
    Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i(TAG, "Work Request Triggered")
        val birthdayList = WhenaboutRepository.get().getBirthdayWhenabouts(Date())


        return  Result.success()
    }

}