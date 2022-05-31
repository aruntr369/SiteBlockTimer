package com.arun.siteblocktimer.utils


class BaseAct private constructor() {
    var data: Boolean = true
    var startTime: String? = ""
    var endTime: String? = ""
    var startTinHH: Int? = 0
    var endTimeHH: Int? = 0
    var websites: List<String> = listOf("facebook.com", "twitter.com", "instagram.com", "reddit.com", "9gag.com/")
    var accessDeniedUrl = "https://ipi.media/wp-content/uploads/2020/06/IPI-Access-to-information-02-06-2020-jpg.jpg"
    companion object {
        val instance = BaseAct()
    }
}