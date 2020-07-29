package com.Shahbaz.schedulerapp.prefrences

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context?) {
    var currentFrag = "current_frag"
    fun getCurrentFrag() = preferences.getInt(currentFrag, 0)
    fun setCurrentFrag(currentFrag: Int) {
        val editor = preferences.edit()
        editor.putInt(this.currentFrag, currentFrag)
        editor.apply()
    }

    private val serviceRuuning = "service_state"
    private val currentIndex = "current_index"


    fun getServiceRunning() = preferences.getBoolean(serviceRuuning, false)
    fun setServiceRunning(time: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(serviceRuuning, time)
        editor.apply()
    }

    fun getCurentIndex() = preferences.getInt(currentIndex, 0)
    fun setCurentIndex(index: Int) {
        val editor = preferences.edit()
        editor.putInt(currentIndex, index)
        editor.apply()
    }

    private val PREF_DB = "Scheduler_Settings"
    private var preferences: SharedPreferences

    init {
        preferences = context!!.getSharedPreferences(PREF_DB, Context.MODE_PRIVATE)
    }
}