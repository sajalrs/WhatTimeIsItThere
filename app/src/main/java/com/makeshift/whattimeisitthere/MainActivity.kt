package com.makeshift.whattimeisitthere

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val workRequest = OneTimeWorkRequest
            .Builder(BirthdayWorker::class.java)
            .build()
        WorkManager.getInstance()
            .enqueue(workRequest)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if(currentFragment == null){
            val fragment = WhenaboutListFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}