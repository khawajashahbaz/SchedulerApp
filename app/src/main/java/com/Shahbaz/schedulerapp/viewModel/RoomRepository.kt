package com.Shahbaz.schedulerapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.Shahbaz.schedulerapp.databaseUitlity.MyAppDatabase
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import java.util.*

class RoomRepository private constructor(context: Context) {
    private val roomDatabase: MyAppDatabase
    val catLivData: MutableLiveData<List<Category?>?>
    val findCat: MutableLiveData<Category?>
    val tasksLiveData: MutableLiveData<HashMap<Int, List<Task?>?>>
    val findTask: MutableLiveData<Task?>
    val doneTasks: MutableLiveData<List<Task?>?>
    val notDoneTasks: MutableLiveData<List<Task?>?>

    @Synchronized
    fun deleteCat(category: Category?) {
        Thread(Runnable {
            roomDatabase.categoryDao()?.deleteCategory(category)
            getAllCat()
        }).start()
    }

    @Synchronized
    fun updateCat(category: Category?) {
        Thread(Runnable {
            roomDatabase.categoryDao()?.editCategory(category)
            getAllCat()
        }).start()
    }

    @Synchronized
    fun addCat(category: Category?) {
        Thread(Runnable {
            roomDatabase.categoryDao()?.addCategory(category)
            getAllCat()
        }).start()
    }

    @Synchronized
    fun findCat(id: Int) {
        Thread(Runnable {
            findCat.postValue(roomDatabase.categoryDao()?.findCategory(id))
        }).start()
    }

    @Synchronized
    fun getAllCat() {
            Thread(Runnable {
                catLivData
                        .postValue(roomDatabase.categoryDao()?.allCategories)
            }).start()
        }

    @Synchronized
    fun getDoneTasks(done: Boolean) {
        Thread(Runnable {
            if (done) {
                doneTasks.postValue(roomDatabase.taskDao()?.getAllTasks(true))
            } else {
                notDoneTasks.postValue(roomDatabase.taskDao()?.getAllTasks(false))
            }
        }).start()
    }

    @Synchronized
    fun getAllTasks() {
            Thread(Runnable {
                val categories = roomDatabase.categoryDao()?.allCategories
                val hashMap = HashMap<Int, List<Task?>?>()
                for (category1 in categories!!) hashMap[category1.id] = roomDatabase.taskDao()?.getCatTasks(category1.id, false)
                tasksLiveData.postValue(hashMap)
            }).start()
        }

    @Synchronized
    fun findTask(task: Task) {
        Thread(Runnable {
            findTask.postValue(roomDatabase
                    .taskDao()?.findTask(task.id))
        }).start()
    }

    @Synchronized
    fun addTask(task: Task?) {
        Thread(Runnable {
            roomDatabase.taskDao()?.addTask(task)
            getAllTasks()
        }).start()
    }

    @Synchronized
    fun deleteTask(task: Task?) {
        Thread(Runnable {
            roomDatabase.taskDao()?.deleteTask(task)
            getAllTasks()
        }).start()
    }

    @Synchronized
    fun updateTask(task: Task?) {
        Thread(Runnable {
            synchronized(roomDatabase) {
                roomDatabase.taskDao()?.editTask(task)
                getAllTasks()
            }
            Log.d(TAG, "updateTask: task updated ")
        }).start()
    }

    @Synchronized
    fun deleteTasks(done: Boolean) {
        Thread(Runnable {
            roomDatabase.taskDao()?.deleteAllTasks(done)
            getDoneTasks(done)
        }).start()
    }

    companion object {
        private var repository: RoomRepository? = null
        private const val TAG = "RoomRepository"
        fun getInstance(context: Context): RoomRepository? {
            if (repository == null) repository = RoomRepository(context)
            return repository
        }
    }

    init {
        roomDatabase = Room.databaseBuilder(context, MyAppDatabase::class.java, "scheduler-app-db").build()
        catLivData = MutableLiveData()
        findCat = MutableLiveData()
        tasksLiveData = MutableLiveData()
        findTask = MutableLiveData()
        doneTasks = MutableLiveData()
        notDoneTasks = MutableLiveData()
    }
}