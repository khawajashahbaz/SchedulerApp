package com.Shahbaz.schedulerapp.databaseUitlity.daos

import androidx.room.*
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category

@Dao
interface CategoryDao {
    @get:Query("SELECT * FROM category_list")
    val allCategories: List<Category>?

    @Query("Select * FROM category_list WHERE :id = category_list.id")
    fun findCategory(id: Int): Category?

    @Update
    fun editCategory(category: Category?)

    @Insert
    fun addCategory(category: Category?)

    @Delete
    fun deleteCategory(category: Category?)
}