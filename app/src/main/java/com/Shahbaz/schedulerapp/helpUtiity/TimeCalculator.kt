package com.Shahbaz.schedulerapp.helpUtiity

object TimeCalculator {
    private const val TAG = "TimeCalculator"

    @Synchronized
    fun CalculateTime(time: String?): Int {
        val seperate = time!!.split(":".toRegex()).toTypedArray()
        val minutes = Integer.valueOf(seperate[0].trim { it <= ' ' })
        val seconds = Integer.valueOf(seperate[1].trim { it <= ' ' })
        return minutes * 60 + seconds
    }

    @Synchronized
    fun CalculateTime(time: MyTimeClassWrapper): Int {
        return time.minutes * 60 + time.seconds
    }

    @Synchronized
    fun formateTime(time: String?): MyTimeClassWrapper {
        val seperate1 = time!!.split(":".toRegex()).toTypedArray()
        val minute = Integer.valueOf(seperate1[0])
        val second = Integer.valueOf(seperate1[1])
        return MyTimeClassWrapper(minute, second)
    }

    @Synchronized
    fun timeDifference(total: String?, done: String?): MyTimeClassWrapper {
        val seperate1 = total!!.split(":".toRegex()).toTypedArray()
        val minute1 = Integer.valueOf(seperate1[0].trim { it <= ' ' })
        val second1 = Integer.valueOf(seperate1[1].trim { it <= ' ' })
        val seperate2 = done!!.split(":".toRegex()).toTypedArray()
        val minute2 = Integer.valueOf(seperate2[0].trim { it <= ' ' })
        val second2 = Integer.valueOf(seperate2[1].trim { it <= ' ' })
        val totalTime1 = minute1 * 60 + second1
        val totalTime2 = minute2 * 60 + second2
        val resultTime = totalTime1 - totalTime2
        val resultMinute = resultTime / 60
        val resultSecond = resultTime % 60
        return MyTimeClassWrapper(resultMinute, resultSecond)
    }
}