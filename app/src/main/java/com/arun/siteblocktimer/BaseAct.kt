package com.arun.siteblocktimer

//class BaseAct {
//    private var isValue : Boolean =true
//
//    constructor(isValue: Boolean) {
//        this.isValue = isValue
//    }
//
//}

class BaseAct private constructor() {
    var data: Boolean? = true
    companion object {
        val instance = BaseAct()
    }
}