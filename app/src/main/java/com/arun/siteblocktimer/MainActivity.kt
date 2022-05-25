package com.arun.siteblocktimer

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var showBottomSheet: LinearLayout
    lateinit var currenttime: String
    lateinit var picker: TimePickerDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val alsoNow = Calendar.getInstance().time
        currenttime = SimpleDateFormat("HH").format(alsoNow)
        var timee = Integer.parseInt(currenttime)


        showBottomSheet = findViewById(R.id.clickToShow)
        showBottomSheet.setOnClickListener {

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
            val fromTime = view.findViewById<EditText>(R.id.followuptime)
            val toTime = view.findViewById<EditText>(R.id.followuptime2)
            val saveTV = view.findViewById<TextView>(R.id.saveTV)

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
    }
}