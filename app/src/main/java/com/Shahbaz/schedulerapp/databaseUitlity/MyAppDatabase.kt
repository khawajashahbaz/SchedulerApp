package com.Shahbaz.schedulerapp.databaseUitlity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.Shahbaz.schedulerapp.databaseUitlity.daos.CategoryDao
import com.Shahbaz.schedulerapp.databaseUitlity.daos.TaskDao
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task

@Database(entities = [Category::class, Task::class], version = 1, exportSchema = false)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao?
    abstract fun taskDao(): TaskDao?
}