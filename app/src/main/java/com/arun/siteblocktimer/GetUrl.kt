package com.arun.siteblocktimer

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Browser
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.arun.siteblocktimer.service.ForegroundService
import java.text.SimpleDateFormat
import java.util.*


class GetUrl : AccessibilityService() {
    private  val TAG = "GetUrl"
    private val previousUrlDetections: HashMap<String, Long> = HashMap()
    override fun onServiceConnected() {
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.packageNames = packageNames()
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        //throttling of accessibility event notification
        info.notificationTimeout = 300
        //support ids interception
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        this.serviceInfo = info
    }

    private fun captureUrl(info: AccessibilityNodeInfo, config: SupportedBrowserConfig): String? {
        val nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId)
        if (nodes == null || nodes.size <= 0) {
            return null
        }
        val addressBarNodeInfo = nodes[0]
        var url: String? = null
        if (addressBarNodeInfo.text != null) {
            url = addressBarNodeInfo.text.toString()
        }
        addressBarNodeInfo.recycle()
        return url
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val parentNodeInfo = event.source ?: return
        val packageName = event.packageName.toString()
        var browserConfig: SupportedBrowserConfig? = null
        for (supportedConfig in supportedBrowsers) {
            if (supportedConfig.packageName == packageName) {
                browserConfig = supportedConfig
            }
        }
        //this is not supported browser, so exit
        if (browserConfig == null) {
            return
        }
        val capturedUrl = captureUrl(parentNodeInfo, browserConfig)
        parentNodeInfo.recycle()

        //we can't find a url. Browser either was updated or opened page without url text field
        if (capturedUrl == null) {
            return
        }
        val eventTime = event.eventTime
        val detectionId = "$packageName, and url $capturedUrl"
        val lastRecordedTime =
            if (previousUrlDetections.containsKey(detectionId)) previousUrlDetections[detectionId]!! else 0
        //some kind of redirect throttling
        if (eventTime - lastRecordedTime > 2000) {
            previousUrlDetections[detectionId] = eventTime
            analyzeCapturedUrl(capturedUrl, browserConfig.packageName)
        }
    }

    private fun timeToStart(capturedUrl: String, browserPackage: String){
//        var alsoNow = Calendar.getInstance().time
//        var currenttime: String = SimpleDateFormat("HH:mm").format(alsoNow)
////        var timee = Integer.parseInt(currenttime)
//        if (baseAct.startTime == currenttime){
//            Log.d(TAG, "timeToStart: Time equals")
//            analyzeCapturedUrl(capturedUrl,browserPackage)
//        }

        val baseAct: BaseAct = BaseAct.instance
        val cur_cal: Calendar = GregorianCalendar()
        cur_cal.timeInMillis =System.currentTimeMillis()


        val cal: Calendar = GregorianCalendar()
//        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR))
        cal[Calendar.HOUR_OF_DAY] = baseAct.startTinHH!!
//        cal[Calendar.MINUTE] = 32
//        cal[Calendar.SECOND] = cur_cal.get(Calendar.SECOND)
//        cal[Calendar.MILLISECOND] = cur_cal.get(Calendar.MILLISECOND)
//        cal[Calendar.DATE] = cur_cal.get(Calendar.DATE)
//        cal[Calendar.MONTH] = cur_cal.get(Calendar.MONTH)
        val intent = Intent(this, ForegroundService::class.java)
        val pintent = PendingIntent.getService(this, 0, intent, 0)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, (2 * 30 * 1000).toLong(), pintent)


    }
    
    private fun analyzeCapturedUrl(capturedUrl: String, browserPackage: String) {
        val redirectUrl = "google.com"
        if (capturedUrl.contains("facebook.com")){
            Log.d(TAG, "analyzeCapturedUrl: URL contain FB")
            performBolck()
        }
        else if (capturedUrl.contains("twitter.com")) {
            performBolck()
        }
        else if (capturedUrl.contains("instagram.com")) {
            performBolck()
        }
        else if (capturedUrl.contains("freddit.com")) {
            performBolck()
        }
        else if (capturedUrl.contains("9gag.com")) {
            //performRedirect(redirectUrl, browserPackage)
            performBolck()
        }
        else{
            val baseAct: BaseAct = BaseAct.instance
            if (baseAct.data == false && capturedUrl.contains(".com")){
                if (!capturedUrl.contains("facebook.com")){
                    Log.d(TAG, "analyzeCapturedUrl: URL contain not fb")
                    intentfun()
                }
                else if (!capturedUrl.contains("twitter.com")) {
                    intentfun()
                }
                else if (!capturedUrl.contains("instagram.com")) {
                    intentfun()
                }
                else if (!capturedUrl.contains("freddit.com")) {
                    intentfun()
                }
                else if (!capturedUrl.contains("9gag.com")) {
                    //performRedirect(redirectUrl, browserPackage)
                    intentfun()
                }
            }
        }
    }
    private fun performBolck(){


        val baseAct: BaseAct = BaseAct.instance
        if (baseAct.data == true){
            intentfun()
        }
    }
    private fun intentfun() {

        val baseAct: BaseAct = BaseAct.instance
        val cur_cal: Calendar = GregorianCalendar()
        cur_cal.timeInMillis =System.currentTimeMillis()


        val cal: Calendar = GregorianCalendar()
        cal[Calendar.HOUR_OF_DAY] = baseAct.startTinHH!!

        val intent = Intent(this, ForegroundService::class.java)
        val pintent = PendingIntent.getService(this, 0, intent, 0)
        val alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, (60 * 1000).toLong(), pintent)


        //   BYdefault //setting follwup date and time to current time and date
        val alsoNow = Calendar.getInstance().time
        var currentdate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(alsoNow)
        var currenttimeHour = SimpleDateFormat("HH", Locale.getDefault()).format(alsoNow)

       // baseAct.endTimeHH
        if (baseAct.endTimeHH == Integer.parseInt(currenttimeHour)){
            stopServ()
        }


    }
    public fun stopServ(){
        val intent = Intent(this, ForegroundService::class.java)
        stopService(intent)
    }

       private fun performRedirect(redirectUrl: String, browserPackage: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            intent.setPackage(browserPackage)
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, browserPackage)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // the expected browser is not installed
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
            startActivity(i)
        }
    }

    override fun onInterrupt() {}
    private class SupportedBrowserConfig(var packageName: String, var addressBarId: String)
    companion object {
        private fun packageNames(): Array<String> {
            val packageNames: MutableList<String> = ArrayList()
            for (config in supportedBrowsers) {
                packageNames.add(config.packageName)
            }
            return packageNames.toTypedArray()
        }

               private val supportedBrowsers: List<SupportedBrowserConfig>
            private get() {
                val browsers: MutableList<SupportedBrowserConfig> = ArrayList()
                browsers.add(
                    SupportedBrowserConfig(
                        "com.android.chrome",
                        "com.android.chrome:id/url_bar"
                    )
                )
                browsers.add(
                    SupportedBrowserConfig(
                        "org.mozilla.firefox",
                        "org.mozilla.firefox:id/url_bar_title"
                    )
                )
                return browsers
            }
    }
}