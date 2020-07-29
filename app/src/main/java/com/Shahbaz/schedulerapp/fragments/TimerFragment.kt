package com.Shahbaz.schedulerapp.fragments

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.activities.MainActivity
import com.Shahbaz.schedulerapp.communication.Done
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.databinding.FragmentTimerBinding
import com.Shahbaz.schedulerapp.helpUtiity.*
import com.Shahbaz.schedulerapp.prefrences.PrefManager
import com.Shahbaz.schedulerapp.timerService.TimeService
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel
import java.util.*

class TimerFragment : Fragment(), Done {
    private var mListener: OnFragmentInteractionListener? = null
    private var binding: FragmentTimerBinding? = null
    private var taskList: List<Task?>? = ArrayList()
    private var timesModes = TimesModes.IDLE
    private var currentIndex = 0
    private var resetTask: Task? = null
    private var viewModel: SchedulerViewModel? = null
    private var prefManager: PrefManager? = null
    private val wrapper = MyTimeClassWrapper(0, 0)
    var first = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)
        viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
        prefManager = PrefManager(context)
        currentIndex = prefManager?.getCurentIndex()?:0
        initializePausePlay()
        initializeIcons(0)
        initializeDone()
        initializeSkip()
        initializeViewModelCycle()
        UpdateFragment.instance?.done = this
        initializeReset()
        return binding?.root
    }

    private fun initializeViewModelCycle() {
        if (first) {
            viewModel?.notDonetasks?.observe(this, Observer { tasks: List<Task?>? ->
                if (tasks != null && tasks.size > 0) {
                    taskList = tasks
                    Log.d(TAG, "initialize: the size is " + taskList!!.size + taskList!![currentIndex]?.done)
                    populateTask(taskList!![currentIndex])
                } else {
                    taskList = ArrayList()
                    Log.d(TAG, "initializeViewModelCycle: list returned is null or have zero size")
                    initializeIcons(5)
                    //                    mListener.onFragmentInteraction(new ActivityWrapper(null,));
                    setNormal()
                }
            })
        }
        first = false
        Log.d(TAG, "initialize: its called twice")
        viewModel!!.populateDoneTasks(false)
    }

    private fun initializeSkip() {
        binding!!.timerSkip.setOnClickListener { v: View? -> perfomSkipClick() }
    }

    private fun initializeReset() {
        binding!!.timerReset.setOnClickListener { v: View? -> performReset() }
    }

    private fun perfomSkipClick() {
        if (timesModes == TimesModes.STOP) {
            performPauseAndPlay()
        }
        afterSkip()
    }

    private fun afterSkip() {
        if (taskList!!.size > currentIndex + 1) ++currentIndex else currentIndex = 0
        prefManager?.setCurentIndex(currentIndex)
        val task = taskList!![currentIndex]
        resetTask = task
        binding?.timerTitle?.text = task?.title
        viewModel?.findCat(task?.catId!!, this, Observer { c: Category? -> changeColor(c?.color!!) })
        val t = TimeCalculator.timeDifference(task?.workingCycle, taskList!![currentIndex]?.minutesWorked)
        binding?.timerTime?.text = t.toString()
        performProgress(task)
        updateWorked(task?.minutesWorked)
    }

    private fun performReset() {
        populateTask(resetTask)
        viewModel?.editTask(resetTask)
    }

    private fun afterDone() {
        if (taskList!!.size - 1 == currentIndex) {
            --currentIndex
            if (currentIndex < 0) currentIndex = 0
            prefManager?.setCurentIndex(currentIndex)
        }
    }

    private fun initializeDone() {
        binding!!.timerDone.setOnClickListener { v: View? -> performDoneClick(5) }
    }

    private fun performDoneClick(i: Int) {
        if (taskList != null && taskList!!.size > 0) {
            if (timesModes == TimesModes.STOP) {
                Log.d(TAG, "performDoneClick: if will be called")
                performPauseAndPlay()
                //                taskList.get(currentIndex).setDone(true);
//                viewModel.editTask(taskList.get(currentIndex));
            } /*else {*/
            //                Log.d(TAG, "performDoneClick: else will be called");
//                if (prefManager.isServiceRunning())
//                    Log.d(TAG, "performDoneClick: " + timesModes);
//                taskList.get(currentIndex).setDone(true);
//                viewModel.editTask(taskList.get(currentIndex));
//            }
            if (i == 6) mListener!!.onFragmentInteraction(ActivityWrapper(null, Modes.STOP_SERVICE))
            taskList!![currentIndex]?.done = true
            viewModel!!.editTask(taskList!![currentIndex])
            afterDone()
            viewModel!!.populateDoneTasks(false)
        } else {
            setNormal()
            initializeIcons(5)
            mListener!!.onFragmentInteraction(ActivityWrapper(null, Modes.STOP_SERVICE))
        }
    }

    private fun updateWorked(time: String?) {
        wrapper.setTime(time)
    }

    private fun populateTask(task: Task?) {
        resetTask = task
        performProgress(task)
        viewModel!!.findCat(task?.catId!!, this, Observer { c: Category? -> changeColor(c?.color!!) })
        binding!!.timerTitle.text = task.title
        binding!!.timerTime.text = TimeCalculator.timeDifference(task.workingCycle, task.minutesWorked).toString()
        updateWorked(task.minutesWorked)
    }

    private fun setNormal() {
//        if (prefManager.isServiceRunning()) {
//            timesModes = TimesModes.IDLE;
//            mListener.onFragmentInteraction(new ActivityWrapper(null, Modes.STOP_SERVICE));
//        }
        resetTask = null
        binding!!.timerTitle.text = "Title"
        binding!!.timerTime.text = "00:00"
        binding!!.timerLoading.progress = 0
        //        binding.timerStop.setIcon(R.drawable.ic_play_arrow);
        changeColor(R.color.colorPrimary)
        initializeIcons(5)
    }

    private fun changeColor(color: Int) {
        binding!!.timerStop.colorPressed = color
        binding!!.layoutTimer.setBackgroundColor(color)
        binding!!.timerDone.setBackgroundColor(color)
        binding!!.timerSkip.setBackgroundColor(color)
        binding!!.timerReset.setBackgroundColor(color)
    }

    fun performProgress(task: Task?) {
        val total = TimeCalculator.CalculateTime(task?.workingCycle) / 100
        val progress = TimeCalculator.CalculateTime(task?.minutesWorked) / total
        Log.d(TAG, "performProgress: " + task?.workingCycle + "  " + task?.minutesWorked + "  " + progress)
        binding!!.timerLoading.progress = progress
    }

    private fun initializePausePlay() {
        binding!!.timerStop.setOnClickListener { v: View? -> performPauseAndPlay() }
    }

    private fun performPauseAndPlay() {
        if (taskList != null && taskList!!.size != 0) {
            timesModes = if (timesModes == TimesModes.IDLE) {
                Log.d(TAG, "performPauseAndPlay: task will Runned again")
                mListener!!.onFragmentInteraction(ActivityWrapper(taskList!![currentIndex], Modes.RUN_SERVICE))
                binding!!.timerStop.setIcon(R.drawable.ic_pause)
                TimesModes.STOP
            } else {
                Log.d(TAG, "performPauseAndPlay: task will be paused")
                val task = taskList!![currentIndex]
                val worked = TimeCalculator.timeDifference(task?.workingCycle, wrapper.toString())
                task?.minutesWorked =worked.toString()
                viewModel!!.editTask(task)
                mListener!!.onFragmentInteraction(ActivityWrapper(null, Modes.STOP_SERVICE))
                binding!!.timerStop.setIcon(R.drawable.ic_play_arrow)
                TimesModes.IDLE
            }
        }
    }

    private fun initializeIcons(i: Int) {
        if (prefManager!!.getServiceRunning()) {
            if (timesModes == TimesModes.IDLE) {
                binding!!.timerStop.setIcon(R.drawable.ic_pause)
                timesModes = TimesModes.STOP
            } else {
                binding!!.timerStop.setIcon(R.drawable.ic_play_arrow)
                timesModes = TimesModes.IDLE
                if (i == 4) performTaskEdit()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    override fun inform(actionType: String) {
        if (actionType == "Finished") {
            val pendingIntent = PendingIntent.getActivity(
                    context, 0, Intent(context, MainActivity::class.java), 0)
            val task = taskList!![currentIndex]
            val notification = NotificationCompat.Builder(context!!, TimeService.Companion.ID)
                    .setSmallIcon(R.drawable.ic_done)
                    .setContentTitle("Task Finished")
                    .setContentText(task?.title + " is finished please click to check")
                    .setContentIntent(pendingIntent)
                    .build()
            performDoneClick(6)
            (context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .notify(11, notification)
        } else if (actionType == "StopService") {
            performCommonTask()
        }
    }

    fun performCommonTask() {
        val task = taskList!![currentIndex]
        val worked = TimeCalculator.timeDifference(task?.workingCycle, wrapper.toString())
        task?.minutesWorked =worked.toString()
        viewModel!!.editTask(task)
        mListener!!.onFragmentInteraction(ActivityWrapper(null, Modes.STOP_SERVICE))
        binding!!.timerStop.setIcon(R.drawable.ic_play_arrow)
        timesModes = TimesModes.IDLE
    }

    override fun notify(minute: Long, seconds: Long, progress: Long) {
        wrapper.minutes = minute.toInt()
        wrapper.seconds = seconds.toInt()
        binding!!.timerTime.text = wrapper.toString()
        binding!!.timerLoading.progress = (100 - progress).toInt()
    }

    override fun pauseNResume() {
        initializeIcons(4)
    }

    private fun performTaskEdit() {
        val task = taskList!![currentIndex]
        val worked = TimeCalculator.timeDifference(task?.workingCycle, wrapper.toString())
        task?.minutesWorked =worked.toString()
        viewModel?.editTask(task)
    }

    override fun skip() {
        perfomSkipClick()
    }

    override fun doneTask() {
        performDoneClick(6)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(activityWrapper: ActivityWrapper)
    }

    companion object {
        private const val TAG = "TimerFragment"
        fun newInstance(): TimerFragment {
            val fragment = TimerFragment()
            val args = Bundle()
            fragment.arguments = Bundle()
            return fragment
        }
    }
}