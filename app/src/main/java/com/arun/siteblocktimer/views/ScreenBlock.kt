package com.arun.siteblocktimer.views

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arun.siteblocktimer.BaseAct
import com.arun.siteblocktimer.R
import com.arun.siteblocktimer.service.ForegroundService


class ScreenBlock : AppCompatActivity() {
    private  val TAG = "ScreenBlock"

    var activity: ScreenBlock? = null

    fun onAttach(activity: Activity?) {
        this.activity = activity as ScreenBlock?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_screen_block)

//        var secondActivity = SecondActivity()
//        if (secondActivity.switch.isChecked){
//        startService()
//        }
//        val baseAct: BaseAct = BaseAct.instance
//        if (baseAct.data == true){
            startService()
//        }else if(baseAct.data == false){
//
//        }

    }

    fun startService() {
        Log.d(TAG, "startService: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if (Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this, ForegroundService::class.java))
                } else {
                    startService(Intent(this, ForegroundService::class.java))
                }
            }
        } else {
            startService(Intent(this, ForegroundService::class.java))
        }
    }

    // method to ask user to grant the Overlay permission
    fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(myIntent)
            }
        }
    }

}