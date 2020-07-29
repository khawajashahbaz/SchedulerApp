package com.Shahbaz.schedulerapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.communication.SelectedCategory
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.databinding.ActivityAddTaskBinding
import com.Shahbaz.schedulerapp.dialogues.SessionDialogue
import com.Shahbaz.schedulerapp.dialogues.ShowTimeDialogue
import com.Shahbaz.schedulerapp.dialogues.ShowcategoryDailogue
import com.Shahbaz.schedulerapp.helpUtiity.Modes
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel

class AddTask : AppCompatActivity(), SelectedCategory {
    var binding: ActivityAddTaskBinding? = null
    var viewModel: SchedulerViewModel? = null
    var categoryDialog: ShowcategoryDailogue? = null
    private var catID = -8
    private var currentMode = Modes.ADD_MMODE
    private var task: Task? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding(savedInstanceState)
        supportActionBar!!.hide()
        initializeLongRest()
        handleEditing()
        handleAddTask()
        handleDelete()
    }

    private fun handleDelete() {
        binding!!.taskDelete.setOnClickListener { v: View? -> showConfirmDialog() }
    }

    fun showConfirmDialog() {
        val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this Task")
                .setPositiveButton("DELETE") { dialogInterface: DialogInterface?, i: Int ->
                    viewModel!!.deleteTask(task)
                    finish()
                }.setNegativeButton("CANCEL") { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.create()
        dialog.show()
    }

    private fun handleEditing() {
        task = Task()
        if (intent.getParcelableExtra<Parcelable?>("TaskClass") != null) {
            binding!!.taskDelete.isEnabled = true
            currentMode = Modes.EDIT_MODE
            task = intent.getParcelableExtra("TaskClass")
            catID = task?.catId!!
            binding!!.repeatWsitch.isSelected = task?.isRepeat!!
            binding!!.titleTaskText.setText(task?.title)
            binding!!.workingTask.text = task?.workingCycle
            binding!!.longRestText.text = task?.longRestInterval
            binding!!.restIntervalText.text = task?.restInterval
            binding!!.longSessionText.text = task?.sessionForLongrest
        } else {
            binding!!.taskDelete.isEnabled = false
        }
    }

    private fun handleAddTask() {
        binding!!.saveLayout.setOnClickListener { v: View? -> }
        binding!!.addTaskSaveButton.setOnClickListener { v: View? ->
            if (textChecked()) {
                task?.done = false
                task?.isRepeat = (binding!!.repeatWsitch.isSelected)
                task?.title = (binding!!.titleTaskText.text.toString().trim { it <= ' ' })
                task?.workingCycle = (binding!!.workingTask.text.toString().trim { it <= ' ' })
                task?.minutesWorked = ("00:00")
                task?.catId = (catID)
                task?.longRestInterval = (binding!!.longRestText.text.toString().trim { it <= ' ' })
                task?.restInterval = (binding!!.restIntervalText.text.toString().trim { it <= ' ' })
                task?.sessionForLongrest = (binding!!.longSessionText.text.toString().trim { it <= ' ' })
                if (currentMode == Modes.ADD_MMODE) {
                    viewModel!!.addTask(task)
                } else {
                    viewModel!!.editTask(task)
                }
                finish()
            }
        }
    }

    private fun textChecked(): Boolean {
        if (binding!!.titleTaskText.text.toString().trim { it <= ' ' }.isEmpty()) {
            binding!!.titleTaskText.error = "Please select Title"
            return false
        }
        if (catID == -8) {
            binding!!.categoryText.error = "Please Select Category"
            return false
        }
        return true
    }

    private fun setBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_task)
        viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
    }

    private fun initializeLongRest() {
        binding!!.sessionForLongRest.setOnClickListener { v: View? -> sessionLongRest() }
        binding!!.longRestInterval.setOnClickListener { v: View? -> sessionLongRestInterval() }
        binding!!.restInterval.setOnClickListener { v: View? -> restInterval() }
        binding!!.workingCycle.setOnClickListener { v: View? -> workingCycle() }
        binding!!.category.setOnClickListener { v: View? -> category() }
    }

    fun category() {
        categoryDialog = ShowcategoryDailogue()
        categoryDialog!!.show(supportFragmentManager, "session")
    }

    fun sessionLongRestInterval() {
        val sessionDialogue = ShowTimeDialogue(Observer { value: String? -> binding!!.longRestText.text = value })
        sessionDialogue.show(supportFragmentManager, "session")
    }

    fun restInterval() {
        val sessionDialogue = ShowTimeDialogue(Observer { value: String? -> binding!!.restIntervalText.text = value })
        sessionDialogue.show(supportFragmentManager, "session")
    }

    fun workingCycle() {
        val sessionDialogue = ShowTimeDialogue(Observer { value: String? -> binding!!.workingTask.text = value })
        sessionDialogue.show(supportFragmentManager, "session")
    }

    fun sessionLongRest() {
        val sessionDialogue = SessionDialogue(Observer { integer: Int -> binding!!.longSessionText.text = integer.toString() })
        sessionDialogue.show(supportFragmentManager, "session")
    }

    override fun selected(s: Category?) {
        binding!!.categoryText.text = s?.icon
        catID = s?.id!!
        categoryDialog!!.dismiss()
        changeColors(s.color)
    }

    private fun changeColors(color: Int) {
        binding!!.l3.setBackgroundColor(color)
        binding!!.l4.setBackgroundColor(color)
        binding!!.addTaskSaveButton.setBackgroundColor(color)
    }

    override fun selected(s: String?) {}
}