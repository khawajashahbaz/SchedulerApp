package com.Shahbaz.schedulerapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import java.util.*

class SchedulerViewModel(application: Application) : AndroidViewModel(application) {
    val categoryLiveData: MutableLiveData<List<Category?>?>?
    var taskLiveData: MutableLiveData<HashMap<Int, List<Task?>?>>
    val donetasks: MutableLiveData<List<Task?>?>?
    val notDonetasks: MutableLiveData<List<Task?>?>?
    private val roomRepository: RoomRepository?
    val findCat: MutableLiveData<Category?>?

    fun populateCat() {
        roomRepository?.getAllCat()
    }

    fun deleteCat(category: Category?) {
        roomRepository?.deleteCat(category)
    }

    fun addCat(category: Category?) {
        roomRepository?.addCat(category)
    }

    fun editCat(category: Category?) {
        roomRepository?.updateCat(category)
    }

    fun findCat(id: Int, owner: LifecycleOwner?, observer: Observer<Category?>?) {
        roomRepository?.findCat?.observe(owner!!, observer!!)
        roomRepository?.findCat(id)
    }

    fun populateTask() {
        roomRepository?.getAllTasks()
    }

    fun deleteTask(task: Task?) {
        roomRepository!!.deleteTask(task)
    }

    fun addTask(task: Task?) {
        roomRepository!!.addTask(task)
    }

    fun editTask(task: Task?) {
        roomRepository!!.updateTask(task)
    }

    fun populateAllTasks() {
        roomRepository?.getAllTasks()
    }

    fun findTask(task: Task, owner: LifecycleOwner?, observer: Observer<Task?>?) {
        roomRepository?.findTask?.observe(owner!!, observer!!)
        roomRepository?.findTask(task)
    }

    fun deleteTasks(done: Boolean) {
        roomRepository?.deleteTasks(done)
    }

    fun populateDoneTasks(done: Boolean) {
        roomRepository?.getDoneTasks(done)
    }

    init {
        taskLiveData = MutableLiveData()
        roomRepository = RoomRepository.getInstance(application.applicationContext)
        taskLiveData = roomRepository?.tasksLiveData!!
        categoryLiveData = roomRepository.catLivData
        donetasks = roomRepository.doneTasks
        notDonetasks = roomRepository.notDoneTasks
        findCat = roomRepository.findCat
    }
}