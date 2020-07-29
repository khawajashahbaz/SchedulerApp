package com.Shahbaz.schedulerapp.helpUtiity

import android.os.CountDownTimer
import android.util.Log
import com.Shahbaz.schedulerapp.communication.Done

class NewTimertest {
    private var minute: Int
    private var seconds: Int
    private var tempSe: Int
    private var tempMin: Int
    private var done: Done
    private var total: Int
    private var tem: Double
    private var timer: CountDownTimer? = null

    constructor(minute: Int, seconds: Int, done: Done) {
        this.minute = minute
        this.seconds = seconds
        tempMin = 0
        tempSe = 0
        total = TimeCalculator.CalculateTime("$minute:$seconds")
        this.done = done
        tem = total / 100.0
        Log.d(TAG, "NewTimertest: $tem")
    }

    constructor(minute: Int, seconds: Int, done: Done, total: Int) {
        this.minute = minute
        this.seconds = seconds
        tempMin = 0
        tempSe = 0
        this.total = total
        this.done = done
        tem = this.total / 100.0
        Log.d(TAG, "NewTimertest: $tem")
    }

    fun setMinute(minute: Int) {
        this.minute = minute
    }

    fun setSeconds(seconds: Int) {
        this.seconds = seconds
    }

    fun start() {
        initialize()
    }

    private fun initialize() {
        val calculate = (minute * 60 + seconds) * 1000
        timer = object : CountDownTimer(calculate.toLong(), 1000) {
            override fun onTick(l: Long) {
                handleUpdate(l)
                val s = (l / 1000).toInt()
                minute = s / 60
                seconds = s % 60
                done.notify(minute.toLong(), seconds.toLong(), (s / tem).toLong())
            }

            override fun onFinish() {
                done.inform("Finished")
            }
        }
        timer?.start()
    }

    private fun handleUpdate(s: Long) {
        ++tempSe
        if (tempSe >= 60) {
            tempSe = 0
            ++tempMin
        }
        minute = s.toInt() / 60
        seconds = s.toInt() % 60
    }

    fun pause(cancel: Boolean) {
        if (timer != null) timer!!.cancel()
        if (!cancel) done.pauseNResume()
        Log.d(TAG, "pausetask: $minute:$seconds")
    }

    fun doneTask() {
        Log.d(TAG, "donetask: $minute:$seconds")
        done.doneTask()
    }

    fun skiptask() {
        Log.d(TAG, "skiptask: $minute:$seconds")
        done.skip()
    }

    fun stop() {
        done.inform("StopService")
    }

    fun restart() {
        Log.d(TAG, "restart: $minute:$seconds")
        if (timer != null) timer!!.cancel()
        initialize()
    }

    companion object {
        private const val TAG = "NewTimertest"
    }
}