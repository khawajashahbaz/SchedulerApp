package com.Shahbaz.schedulerapp.helpUtiity

class MyTimeClassWrapper {
    var minutes = 0
    var seconds = 0

    constructor(time: String?) {
        setTime(time)
    }

    constructor(minutes: Int, seconds: Int) {
        this.minutes = minutes
        this.seconds = seconds
    }

    fun setTime(time: String?) {
        val s = TimeCalculator.formateTime(time)
        minutes = s!!.minutes
        seconds = s.seconds
    }

    fun incrementMinute(mode: Int) {
        if (mode == DECREMENT) {
            --minutes
        } else {
            ++minutes
        }
    }

    fun incrementSecond(mode: Int) {
        if (mode == DECREMENT) {
            --seconds
            if (seconds <= 0) {
                seconds = 59
                incrementMinute(DECREMENT)
            }
        } else {
            ++seconds
            if (seconds >= 60) {
                seconds = 0
                incrementMinute(INCREMENT)
            }
        }
    }

    override fun toString(): String {
        var m = minutes.toString() + ""
        var s = seconds.toString() + ""
        if (minutes < 10) m = "0$minutes"
        if (seconds < 10) s = "0$seconds"
        return "$m:$s"
    }

    companion object {
        var DECREMENT = 8
        var INCREMENT = 11
    }
}