package com.arun.siteblocktimer


class BaseAct private constructor() {
    var data: Boolean = true
    var startTime: String? = ""
    var startTinHH: Int? = 0
    var endTimeHH: Int? = 0
    companion object {
        val instance = BaseAct()
    }
}