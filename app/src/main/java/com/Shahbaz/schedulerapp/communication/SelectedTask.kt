package com.Shahbaz.schedulerapp.communication

import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task

interface SelectedTask {
    fun inform(task: Task?)
}