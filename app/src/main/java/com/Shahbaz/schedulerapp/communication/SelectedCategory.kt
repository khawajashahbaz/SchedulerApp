package com.Shahbaz.schedulerapp.communication

import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category

interface SelectedCategory {
    fun selected(s: Category?)
    fun selected(s: String?)
}