package com.makeshift.whattimeisitthere

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

private const val TAG = "BirthdayWorker"
class BirthdayWorker(val context:Context, workerParams: WorkerParameters):
    Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.i(TAG, "Work Request Triggered")
        val birthdayList = WhenaboutRepository.get().getBirthdayWhenabouts(Date())
        if(!(birthdayList.isEmpty())){
            var displayString = ""
            for(birthday in birthdayList){
                displayString += (birthday.name + "\n")
            }

            val intent = MainActivity.newIntent(context)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent,0)
            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL_ID)
                .setTicker(resources.getString(R.string.daily_birthday)).setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.daily_birthday))
                .setContentText(displayString)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val notificationManager =
                NotificationManagerCompat.from(context)

            notificationManager.notify(0, notification)
        }


        return  Result.success()
    }

}