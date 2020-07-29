package com.Shahbaz.schedulerapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.CategoryIconAdapter.CategoryIconViewHolder
import com.Shahbaz.schedulerapp.communication.SelectedCategory
import com.Shahbaz.schedulerapp.customTextView.CustomTextView

class CategoryIconAdapter(private val context: Context?, list: List<Int>) : RecyclerView.Adapter<CategoryIconViewHolder>() {
    private val category: SelectedCategory?
    private val list: List<Int>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryIconViewHolder {
        return CategoryIconViewHolder(LayoutInflater.from(context).inflate(R.layout.category_icon_list, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryIconViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: $position")
        holder.title.setText("${list[position]}")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class CategoryIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: CustomTextView

        init {
            itemView.setOnClickListener { v: View? -> category?.selected("${list[adapterPosition]}") }
            title = itemView.findViewById(R.id.category_icon_text)
        }
    }

    companion object {
        private const val TAG = "CategoryIconAdapter"
    }

    init {
        category = context as SelectedCategory?
        this.list = list
    }
}