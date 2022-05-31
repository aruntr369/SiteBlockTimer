package com.arun.siteblocktimer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.arun.siteblocktimer.utils.BaseAct
import java.time.LocalTime
import java.util.*


class GetUrl : AccessibilityService() {
    private val TAG = "GetUrl"
    val baseAct: BaseAct = BaseAct.instance
    var capturedUrl: String? = null
    private val previousUrlDetections: HashMap<String, Long> = HashMap()
    override fun onServiceConnected() {
        val info = serviceInfo
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.packageNames = packageNames()
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        info.notificationTimeout = 300
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
        capturedUrl = captureUrl(parentNodeInfo, browserConfig)
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
            analyzeCapturedUrl(capturedUrl!!, browserConfig.packageName)
        }
    }

    private fun analyzeCapturedUrl(capturedUrl: String, browserPackage: String) {
        if (capturedUrl != null) {
            Log.d(TAG, "onAccessibilityEvent: $capturedUrl")
            if (isScheduledTimeNow()) {

                if (isListedUrl(capturedUrl)) {
                    if (baseAct.data)
                        launchAccessDenied(capturedUrl)
                }
                else {
                    if (!baseAct.data)
                        launchAccessDenied(capturedUrl)
                }
            }
        }
    }

    private fun isListedUrl(capturedUrl: String): Boolean  {
        var isContains = false
        baseAct.websites.forEach {
            if (capturedUrl.contains(it)) {
                isContains = true
                return@forEach
            }
        }
        return isContains
    }

    private fun isScheduledTimeNow() : Boolean {

        val currentTime = LocalTime.now()
        val scheduleStart = LocalTime.parse(baseAct.startTime)
        val scheduleEnd = LocalTime.parse(baseAct.endTime)

        return (currentTime.isAfter(scheduleStart) && currentTime.isBefore(scheduleEnd))
    }

    private fun launchAccessDenied(currentURL: String) {

        if (currentURL.contains("ipi.media/wp-content/uploads") || currentURL.contains("Search or type")) return

        if (!Patterns.WEB_URL.matcher(currentURL).matches()) return

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("googlechrome://navigate?url=${baseAct.accessDeniedUrl}")
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            applicationContext.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
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
                return browsers
            }
    }
}