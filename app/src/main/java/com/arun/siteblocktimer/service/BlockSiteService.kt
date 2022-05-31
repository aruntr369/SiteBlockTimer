package com.arun.siteblocktimer.service

import android.accessibilityservice.AccessibilityService
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.time.LocalTime

class BlockSiteService : AccessibilityService()  {
    private val TAG = "BlockSiteService"
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val parentNodeInfo = event!!.source ?: return
        val packageName = event!!.packageName.toString()
        Log.d(TAG, "onAccessibilityEvent: Yessss")

        if ("com.android.chrome".equals(packageName)) {
            val capturedUrl: String? = captureUrl(parentNodeInfo)
            if (capturedUrl != null) {
                Log.d(TAG, "onAccessibilityEvent: $capturedUrl")
//                val blacklistMode = AppPreferences.getBlackListMode(applicationContext)
//                if (isScheduledTimeNow()) {
//
//                    if (isListedUrl(capturedUrl)) {
//                        if (blacklistMode)
//                            launchAccessDenied(capturedUrl)
//                    }
//                    else {
//                        if (!blacklistMode)
//                            launchAccessDenied(capturedUrl)
//                    }
//                }

            }
        }
    }

    private fun captureUrl(info: AccessibilityNodeInfo): String? {
        val nodes = info.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar")
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

//    private fun launchAccessDenied(currentURL: String) {
//
//        if (currentURL.contains("ipi.media/wp-content/uploads") || currentURL.contains("Search or type")) return
//
//        if (!Patterns.WEB_URL.matcher(currentURL).matches()) return
//
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("googlechrome://navigate?url=${Constants.accessDeniedUrl}"))
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.setPackage("com.android.chrome")
//        try {
//            applicationContext.startActivity(intent)
//        } catch (ex: ActivityNotFoundException) {
//        }
//    }
//
//    private fun isScheduledTimeNow() : Boolean {
//
//        val currentTime = LocalTime.now()
//        val scheduleStart = Utils.getLocalTime(AppPreferences.getValueFromSharedPreference(applicationContext, Constants.TIME_FROM))
//        val scheduleEnd = Utils.getLocalTime(AppPreferences.getValueFromSharedPreference(applicationContext, Constants.TIME_TO))
//
//        return (currentTime.isAfter(scheduleStart) && currentTime.isBefore(scheduleEnd))
//    }

    override fun onInterrupt() {}
}