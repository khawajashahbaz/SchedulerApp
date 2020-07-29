package com.Shahbaz.schedulerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.CategoryAdapter.CategoryViewHolder
import com.Shahbaz.schedulerapp.communication.SelectedCategory
import com.Shahbaz.schedulerapp.customTextView.CustomTextView
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import de.hdodenhof.circleimageview.CircleImageView

class CategoryAdapter(private var list: List<Category?>?, private val context: Context?) : RecyclerView.Adapter<CategoryViewHolder>() {
    private val category: SelectedCategory?
    fun setList(list: List<Category?>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.categories_list, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.desc.text = list!![position]?.name
        holder.icon.text = list!![position]?.icon
        holder.circleImageView.setColorFilter(list!![position]?.color!!)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: CustomTextView
        val desc: TextView
        val circleImageView: CircleImageView

        init {
            itemView.setOnClickListener { v: View? -> category!!.selected(list!![adapterPosition]) }
            icon = itemView.findViewById(R.id.category_list_text)
            desc = itemView.findViewById(R.id.category_list_desc)
            circleImageView = itemView.findViewById(R.id.category_list_circular)
        }
    }

    init {
        category = context as SelectedCategory?
    }
}