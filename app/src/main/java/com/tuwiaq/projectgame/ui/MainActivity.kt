package com.tuwiaq.projectgame.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

import androidx.work.*
import com.tuwiaq.projectgame.R

import java.util.*
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // prevent the user from taking screen shot
       window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_main)
        var sher = this.getSharedPreferences("My_pref", MODE_PRIVATE)
        val lang = sher.getString("My_Lang", "")
        lang.let {
           setLocle(lang.toString())
            // share view  to all act
        }

        myWorkerManger()
    }
    private fun setLocle(s: String) {
        val loc = Locale(s)
        Locale.setDefault(loc)
        val conf = Configuration()
        conf.locale = loc
        this.resources.updateConfiguration(
            conf,
            this.resources.displayMetrics
        )
        var sharedpref1 = this.getSharedPreferences("My_pref", MODE_PRIVATE)
        var editor = sharedpref1.edit()
        editor.putString("My_Lang", s)
        editor.apply()

    }


    private fun myWorkerManger() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()
        val myRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,24, TimeUnit.HOURS
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
