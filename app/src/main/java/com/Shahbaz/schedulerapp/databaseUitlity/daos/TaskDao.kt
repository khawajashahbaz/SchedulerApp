package com.Shahbaz.schedulerapp.databaseUitlity.daos

import androidx.room.*
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_list WHERE task_list.done = :done")
    fun getAllTasks(done: Boolean): List<Task>?

    @Query("Select * FROM task_list WHERE task_list.id = :id")
    fun findTask(id: Int): Task?

    @Update
    fun editTask(Task: Task?)

    @Insert
    fun addTask(Task: Task?)

    @Delete
    fun deleteTask(Task: Task?)

    @Query("DELETE FROM task_list WHERE task_list.done = :done")
    fun deleteAllTasks(done: Boolean)

    @Query("SELECT * FROM task_list WHERE (cat_id = :catID AND task_list.done = :done)")
    fun getCatTasks(catID: Int, done: Boolean): List<Task>?
}