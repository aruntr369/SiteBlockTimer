package com.arun.siteblocktimer.views

import android.app.Activity
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.arun.siteblocktimer.R
import com.arun.siteblocktimer.service.BlockService
import com.arun.siteblocktimer.service.ForegroundService
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import android.content.Intent
import android.provider.Settings


class SecondActivity : AppCompatActivity() {
    private  val TAG = "SecondActivityy"

    lateinit var fromtv: TextView
    lateinit var totv: TextView
    private lateinit var deleteTV: TextView
    private lateinit var editTV: TextView
    var fromTime: String =""
    var toTime: String =""
    private lateinit var currenttime: String
    lateinit var picker: TimePickerDialog
    lateinit var switch: SwitchCompat
    lateinit var tvBlack: TextView
    lateinit var tvWhite: TextView
    lateinit var tvWhiteList: TextView
    lateinit var tvBlackList: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        checkOverlayPermission()

        fromtv = findViewById(R.id.fromTime2)
        totv = findViewById(R.id.toTime2)
        deleteTV= findViewById(R.id.deleteTV)
        editTV = findViewById(R.id.editTV)
        switch = findViewById(R.id.switch1)
        tvBlack = findViewById(R.id.tvBlack)
        tvWhite = findViewById(R.id.tvWhite)
        tvWhiteList = findViewById(R.id.tvWhiteList)
        tvBlackList = findViewById(R.id.tvBlackList)

        blackList()

        val i = intent
        fromTime = i.getStringExtra("fromTime").toString()
        toTime = i.getStringExtra("toTime").toString()

        fromtv.text = fromTime
        totv.text = toTime

        deleteTV.setOnClickListener {
            //val ii = Intent(applicationContext,MainActivity::class.java)
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }
        editTV.setOnClickListener {
            toBottomSheet()
        }
        switch.isChecked = true
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { button, b ->
            Log.d(TAG, "onCreate: change")
            if (!switch.isChecked){
                whiteList()

            }else if (switch.isChecked){
                blackList()

            }
        })
    }

    fun whiteList(){
        tvBlack.visibility = View.GONE
        tvWhite.visibility = View.VISIBLE
        tvWhiteList.setTextColor(Color.parseColor("#FF000000"))
        tvBlackList.setTextColor(Color.parseColor("#73777B"))
    }
    fun blackList(){
        tvBlack.visibility = View.VISIBLE
        tvWhite.visibility = View.GONE
        tvBlackList.setTextColor(Color.parseColor("#FF000000"))
        tvWhiteList.setTextColor(Color.parseColor("#73777B"))

    }


    private fun toBottomSheet() {

        val alsoNow = Calendar.getInstance().time
        currenttime = SimpleDateFormat("HH").format(alsoNow)
        var timee = Integer.parseInt(currenttime)

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
        val fromTime = view.findViewById<EditText>(R.id.followuptime)
        val toTime = view.findViewById<EditText>(R.id.followuptime2)
        val saveTV = view.findViewById<TextView>(R.id.saveTV)
        val bottomHead = view.findViewById<TextView>(R.id.bottomHead)

        bottomHead.text ="Edit Timing"

        fromTime.setText(timee.toString()+":00")
        toTime.setText((timee+1).toString()+":00")
        fromTime.setOnClickListener {
            picker = TimePickerDialog(this,
                { timePicker, h, m -> fromTime.setText("$h:$m") }, timee, 0, true
            )
            picker.show()
        }
        toTime.setOnClickListener {
            picker = TimePickerDialog(this,
                { timePicker, h, m -> toTime.setText("$h:$m") }, timee+1, 0, true
            )
            picker.show()
        }
        saveTV.setOnClickListener {
            val i = Intent(applicationContext, SecondActivity::class.java)
            i.putExtra("fromTime",fromTime.text.toString())
            i.putExtra("toTime",toTime.text.toString())
            startActivity(i)
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


    fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(myIntent)
            }
        }
    }



}