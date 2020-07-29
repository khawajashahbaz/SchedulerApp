package com.Shahbaz.schedulerapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.fragments.DoneFragment
import com.Shahbaz.schedulerapp.fragments.TimerFragment
import com.Shahbaz.schedulerapp.fragments.TodoFragment
import com.Shahbaz.schedulerapp.helpUtiity.ActivityWrapper
import com.Shahbaz.schedulerapp.helpUtiity.Modes
import com.Shahbaz.schedulerapp.prefrences.PrefManager
import com.Shahbaz.schedulerapp.timerService.TimeService

class MainActivity : AppCompatActivity(), TodoFragment.OnFragmentInteractionListener, DoneFragment.OnFragmentInteractionListener, TimerFragment.OnFragmentInteractionListener {
    var timerFragment: TimerFragment = TimerFragment.Companion.newInstance()
    var doneFragment: DoneFragment = DoneFragment.Companion.newInstance("search", "search")
    var todoFragment: TodoFragment = TodoFragment.Companion.newInstance("search", "search")
    private var manager: PrefManager? = null
    private fun startTimerFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, timerFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun startDoneFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, doneFragment)
                .addToBackStack(null)
                .commit()
    }

    private fun startTodoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, todoFragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        manager = PrefManager(this)
        val i = manager?.getCurrentFrag()
        when (i) {
            0 -> {
                navView.menu.getItem(0).isChecked = true
                startTodoFragment()
            }
            1 -> {
                navView.menu.getItem(1).isChecked = true
                startTimerFragment()
            }
            2 -> {
                navView.menu.getItem(2).isChecked = true
                startDoneFragment()
            }
        }
        navView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startTodoFragment()
                    manager?.setCurrentFrag(0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    startTimerFragment()
                    manager?.setCurrentFrag(1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    startDoneFragment()
                    manager?.setCurrentFrag(2)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    override fun onFragmentInteraction(uri: Uri?) {}
    override fun onFragmentInteraction(activityWrapper: ActivityWrapper) {
        if (activityWrapper.modes == Modes.RUN_SERVICE) {
            manager?.setServiceRunning(true)
            Log.d(TAG, "onPropertyChanged: will be started")
            startService(Intent(this@MainActivity, TimeService::class.java)
                    .putExtra("task", activityWrapper.task).putExtra("token", 7))
        } else if (activityWrapper.modes == Modes.STOP_SERVICE) {
            manager?.setServiceRunning(false)
            Log.d(TAG, "onPropertyChanged: service will be stoped")
            startService(Intent(this@MainActivity, TimeService::class.java).putExtra("token", 5))
        } else if (activityWrapper.modes == Modes.EDIT_MODE) {
            manager?.setServiceRunning(true)
            Log.d(TAG, "onPropertyChanged: service will be stoped")
            startService(Intent(this@MainActivity, TimeService::class.java)
                    .putExtra("task", activityWrapper.task).putExtra("token", 8))
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}