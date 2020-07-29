package com.Shahbaz.schedulerapp.timerService

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.activities.MainActivity
import com.Shahbaz.schedulerapp.helpUtiity.UpdateFragment

class NotActionReciever : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: " + intent.getStringExtra("action"))
        if (intent.getStringExtra("action") == "PauseService") {
            UpdateFragment.timertest?.pause(false)
            showResume(context, 0)
        } else if (intent.getStringExtra("action") == "DoneService") {
            UpdateFragment.timertest?.pause(false)
            UpdateFragment.timertest?.doneTask()
        } else if (intent.getStringExtra("action") == "SkipService") {
            UpdateFragment.timertest?.pause(false)
            UpdateFragment.timertest?.skiptask()
        } else if (intent.getStringExtra("action") == "PlayService") {
            UpdateFragment.timertest?.restart()
            showResume(context, 1)
        } else if (intent.getStringExtra("action") == "StopService") {
            UpdateFragment.timertest?.stop()
        } else {
            Log.d(TAG, "onReceive: " + intent.getStringExtra("action"))
        }
    }

    companion object {
        private const val TAG = "NotActionReciever"
        fun showResume(context: Context, i: Int) {
            val pendingIntent = PendingIntent.getActivity(
                    context, 0, Intent(context, MainActivity::class.java), 0)
            TimeService.Companion.customNotification = NotificationCompat.Builder(context.applicationContext, TimeService.Companion.ID)
                    .setSmallIcon(R.drawable.ic_done)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setContentIntent(pendingIntent)
                    .setSound(null)
                    .setOnlyAlertOnce(true)
                    .setCustomBigContentView(if (i == 0) TimeService.Companion.resumeNotification else TimeService.Companion.notificationLayoutExpanded)
                    .build()
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(TimeService.Companion.NOTIFICATION_ID, TimeService.Companion.customNotification)
        }
    }
}