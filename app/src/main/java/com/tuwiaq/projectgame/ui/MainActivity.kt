package com.tuwiaq.projectgame.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.tuwiaq.projectgame.R
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        myWorkerManger()
    }

    private fun myWorkerManger() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()
        val myRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,15, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP,myRequest)
    }

    private fun simpleWork() {
        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>().build()
        WorkManager.getInstance(this).enqueue(mRequest)
    }



}
