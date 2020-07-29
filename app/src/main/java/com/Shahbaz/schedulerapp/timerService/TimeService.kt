package com.Shahbaz.schedulerapp.timerService

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.activities.MainActivity
import com.Shahbaz.schedulerapp.communication.Done
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.helpUtiity.NewTimertest
import com.Shahbaz.schedulerapp.helpUtiity.TimeCalculator
import com.Shahbaz.schedulerapp.helpUtiity.UpdateFragment
import com.Shahbaz.schedulerapp.prefrences.PrefManager

class TimeService : Service() {
    private var timer: NewTimertest? = null
    private var task: Task? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ID, "schedularApp", importance)
            //            channel.setSound(null,null);
//            channel.enableLights(false);
//            channel.setLightColor(Color.BLUE);
//            channel.enableVibration(false);
            channel.description = "This service is for calculating time when app is not running"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(baseContext, MainActivity::class.java), 0)
        notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_view)
        resumeNotification = RemoteViews(packageName, R.layout.notification_layout_2)
        customNotification = NotificationCompat.Builder(applicationContext, ID)
                .setSmallIcon(R.drawable.ic_done)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setContentIntent(pendingIntent)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setCustomBigContentView(notificationLayoutExpanded)
                .build()
        if (intent != null) if (intent.getIntExtra("token", 6) == 5) {
            if (UpdateFragment.timertest != null) UpdateFragment.timertest?.pause(true)
            stopForeground(true)
            stopSelf()
        } else {
            task = intent.getParcelableExtra("task")
            notificationLayoutExpanded?.setTextViewText(R.id.notification_title, task?.title)
            startForeground(NOTIFICATION_ID, customNotification)
            setUp(intent)
        }
        return START_STICKY
    }

    fun setTimer() {
        val wrapper = TimeCalculator.timeDifference(task?.workingCycle, task?.minutesWorked)
        timer = NewTimertest(wrapper.minutes, wrapper.seconds, object : Done {
            override fun inform(actionType: String) {
                UpdateFragment.instance?.done?.inform(actionType)
            }

            override fun notify(minute: Long, seconds: Long, progress: Long) {
                UpdateFragment.instance?.done?.notify(minute, seconds, progress)
                val text = ("Time left: " + (if (minute > 9) "" + minute else "0$minute")
                        + ":" + if (seconds > 9) "" + seconds else "0$seconds")
                notificationLayoutExpanded?.setTextViewText(R.id.time_left, text)
                notificationLayoutExpanded?.setProgressBar(R.id.notification_Progress_bar, 100,
                        (100 - progress).toInt(), false)
                updateNotification()
            }

            override fun pauseNResume() {
                UpdateFragment.instance?.done?.pauseNResume()
            }

            override fun skip() {
                timer!!.pause(false)
                UpdateFragment.instance?.done?.skip()
            }

            override fun doneTask() {
                UpdateFragment.instance?.done?.doneTask()
            }
        }, TimeCalculator.CalculateTime(task?.workingCycle))
        UpdateFragment?.timertest =timer
    }

    private fun setUp(intent: Intent) {
        setTimer()
        if (intent.getIntExtra("token", 6) != 8) {
            timer!!.restart()
        } else {
            val wrapper = TimeCalculator.timeDifference(task?.workingCycle, task?.minutesWorked)
            val text = "Time left: $wrapper"
            notificationLayoutExpanded!!.setTextViewText(R.id.time_left, text)
            val progress = TimeCalculator.CalculateTime(task?.minutesWorked) /
                    (TimeCalculator.CalculateTime(task?.workingCycle) / 100)
            notificationLayoutExpanded!!.setProgressBar(R.id.notification_Progress_bar, 100,
                    progress, false)
        }
        val pause = PendingIntent
                .getBroadcast(applicationContext, 5, Intent(applicationContext, NotActionReciever::class.java)
                        .putExtra("action", "PauseService"), 0)
        notificationLayoutExpanded!!.setOnClickPendingIntent(R.id.not_pause, pause)
        val done = PendingIntent
                .getBroadcast(applicationContext, 8, Intent(applicationContext, NotActionReciever::class.java)
                        .putExtra("action", "DoneService"), 0)
        notificationLayoutExpanded!!.setOnClickPendingIntent(R.id.not_done, done)
        val skip = PendingIntent
                .getBroadcast(applicationContext, 9, Intent(applicationContext, NotActionReciever::class.java)
                        .putExtra("action", "SkipService"), 0)
        notificationLayoutExpanded!!.setOnClickPendingIntent(R.id.not_skip, skip)
        val resume = PendingIntent
                .getBroadcast(applicationContext, 6, Intent(applicationContext, NotActionReciever::class.java)
                        .putExtra("action", "PlayService"), 0)
        resumeNotification!!.setOnClickPendingIntent(R.id.not_resume, resume)
        val stop = PendingIntent
                .getBroadcast(applicationContext, 7, Intent(applicationContext, NotActionReciever::class.java)
                        .putExtra("action", "StopService"), 0)
        resumeNotification!!.setOnClickPendingIntent(R.id.not_stop, stop)
        updateNotification()
    }

    fun updateNotification() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(NOTIFICATION_ID, customNotification)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) timer!!.pause(true)
        PrefManager(applicationContext).setServiceRunning(false)
    }

    companion object {
        const val ID = "scheduler_654"
        const val NOTIFICATION_ID = 774
        private const val TAG = "TimeService"
        var notificationLayoutExpanded: RemoteViews? = null
        var customNotification: Notification? = null
        var resumeNotification: RemoteViews? = null
    }
}