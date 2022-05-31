package com.arun.siteblocktimer.views

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.arun.siteblocktimer.BaseAct
import com.arun.siteblocktimer.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class SecondActivity : AppCompatActivity() {
    private  val TAG = "SecondActivityy"

    lateinit var fromtv: TextView
    lateinit var totv: TextView
    private lateinit var deleteTV: TextView
    private lateinit var editTV: TextView
    var fromTime: String =""
    var toTime: String =""
    private lateinit var Hcurrenttime: String
    private lateinit var Mcurrenttime: String
    lateinit var picker: TimePickerDialog
    lateinit var switch: SwitchCompat
    lateinit var tvBlack: TextView
    lateinit var tvWhite: TextView
    lateinit var tvWhiteList: TextView
    lateinit var tvBlackList: TextView
    val baseAct: BaseAct = BaseAct.instance




    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //checkOverlayPermission()

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


        val outputFormatHH: DateFormat = SimpleDateFormat("HH")
        val outputFormatmm: DateFormat = SimpleDateFormat("mm")

        val inputFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.US)
        val inputText: String = fromTime
        var dateFrom: Date? = null
        try {
            dateFrom = inputFormat.parse(inputText)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val outputTextHH: String = outputFormatHH.format(dateFrom)
        val outputTextmm: String = outputFormatmm.format(dateFrom)

        val inputTextTO: String = toTime
        var dateFromTO: Date? = null
        try {
            dateFromTO = inputFormat.parse(inputTextTO)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val outputTextHHTO: String = outputFormatHH.format(dateFromTO)
        val outputTextmmTO: String = outputFormatmm.format(dateFromTO)




        Log.d(TAG, "onCreate: timesFromTV $outputTextHH : $outputTextmm")
        Log.d(TAG, "onCreate: timesFromTV $outputTextHHTO : $outputTextmmTO")

        baseAct.startTime = "$outputTextHH:$outputTextmm"
        baseAct.endTime = "$outputTextHHTO:$outputTextmmTO"


        baseAct.startTinHH = Integer.parseInt(outputTextHH)
        baseAct.endTimeHH = Integer.parseInt(outputTextHHTO)
        Log.d(TAG, "onCreate: to time "+Integer.parseInt(outputTextHHTO))

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
        baseAct.data = false

        tvBlack.visibility = View.GONE
        tvWhite.visibility = View.VISIBLE
        tvWhiteList.setTextColor(Color.parseColor("#FF000000"))
        tvBlackList.setTextColor(Color.parseColor("#73777B"))
    }
    fun blackList(){
        baseAct.data = true

        tvBlack.visibility = View.VISIBLE
        tvWhite.visibility = View.GONE
        tvBlackList.setTextColor(Color.parseColor("#FF000000"))
        tvWhiteList.setTextColor(Color.parseColor("#73777B"))

    }


    private fun toBottomSheet() {

        val alsoNow = Calendar.getInstance().time
        Hcurrenttime = SimpleDateFormat("HH").format(alsoNow)
        Mcurrenttime = SimpleDateFormat("mm").format(alsoNow)
        var timee = Integer.parseInt(Hcurrenttime)
        var timeeMM = Integer.parseInt(Mcurrenttime)
        Log.d(TAG, "onCreate:Times are $timeeMM  ,, $timee")

        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
        val fromTime = view.findViewById<EditText>(R.id.followuptime)
        val toTime = view.findViewById<EditText>(R.id.followuptime2)
        val saveTV = view.findViewById<TextView>(R.id.saveTV)
        val bottomHead = view.findViewById<TextView>(R.id.bottomHead)

        bottomHead.text ="Edit Timing"

        fromTime.setText(timee.toString()+":"+timeeMM.toString())
        toTime.setText((timee+1).toString()+":"+timeeMM.toString())
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
            finish()
            dialog.dismiss()
            startActivity(i)
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