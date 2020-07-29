package com.Shahbaz.schedulerapp.communication

interface Done {
    fun inform(actionType: String)
    fun notify(minute: Long, seconds: Long, progress: Long)
    fun pauseNResume()
    fun skip()
    fun doneTask()
}