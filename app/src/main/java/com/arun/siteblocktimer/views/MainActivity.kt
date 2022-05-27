package com.arun.siteblocktimer.views

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arun.siteblocktimer.service.BlockService
import com.arun.siteblocktimer.R
import com.arun.siteblocktimer.service.ForegroundService
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var showBottomSheet: LinearLayout
    private lateinit var Hcurrenttime: String
    private lateinit var Mcurrenttime: String
    lateinit var picker: TimePickerDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))


        var accessEnabled = 0
        try {
            accessEnabled = Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (accessEnabled == 0) {
            var intent =Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }


//        checkOverlayPermission()
//        startService()















        val alsoNow = Calendar.getInstance().time
        Hcurrenttime = SimpleDateFormat("HH").format(alsoNow)
        Mcurrenttime = SimpleDateFormat("mm").format(alsoNow)
        var timee = Integer.parseInt(Hcurrenttime)
        var timeeMM = Integer.parseInt(Mcurrenttime)
        Log.d(TAG, "onCreate:Times are $timeeMM  ,, $timee")


        showBottomSheet = findViewById(R.id.clickToShow)
        showBottomSheet.setOnClickListener {

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
            val fromTime = view.findViewById<EditText>(R.id.followuptime)
            val toTime = view.findViewById<EditText>(R.id.followuptime2)
            val saveTV = view.findViewById<TextView>(R.id.saveTV)

            fromTime.setText(timee.toString()+":"+timeeMM.toString())
            toTime.setText((timee+1).toString()+":"+timeeMM.toString())
            fromTime.setOnClickListener {
                picker = TimePickerDialog(this,
                    { timePicker, h, m -> fromTime.setText("$h:$m") }, timee, timeeMM, true
                )
                picker.show()
            }
            toTime.setOnClickListener {
                picker = TimePickerDialog(this,
                    { timePicker, h, m -> toTime.setText("$h:$m") }, timee+1, timeeMM, true
                )
                picker.show()
            }
            saveTV.setOnClickListener {
                val i = Intent(applicationContext, SecondActivity::class.java)
                i.putExtra("fromTime",fromTime.text.toString())
                i.putExtra("toTime",toTime.text.toString())
                startActivity(i)
                dialog.dismiss()
                finish()
            }
            val btnClose = view.findViewById<ImageView>(R.id.closeIV)
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }
    }

}


