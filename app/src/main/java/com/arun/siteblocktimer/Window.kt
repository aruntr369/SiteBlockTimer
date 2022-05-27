package com.arun.siteblocktimer


import android.content.Context
import android.content.Intent
import android.os.Build
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import com.arun.siteblocktimer.R
import com.arun.siteblocktimer.service.ForegroundService
import java.lang.Exception

class Window(
    private val context: Context
) {
    private val mView: View
    private var mParams: WindowManager.LayoutParams? = null
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater
    fun open() {
        try {
            if (mView.windowToken == null) {
                if (mView.parent == null) {
                    mWindowManager.addView(mView, mParams)
                }
            }
        } catch (e: Exception) {
            Log.d("Error1", e.toString())
        }
    }

    fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
            mView.invalidate()
            (mView.parent as ViewGroup).removeAllViews()

            var getUrl = GetUrl()
            getUrl.stopServ()


        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,  // Display it on top of other application windows
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
                PixelFormat.TRANSLUCENT
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.popup_window, null)

        mView.findViewById<View>(R.id.window_close).setOnClickListener { close() }

        mParams!!.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
}