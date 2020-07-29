package com.Shahbaz.schedulerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.DoneTaskAdapter.DoneViewHolder
import com.Shahbaz.schedulerapp.communication.SelectedTask
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import com.Shahbaz.schedulerapp.fragments.DoneFragment
import de.hdodenhof.circleimageview.CircleImageView

class DoneTaskAdapter(private var list: List<Task?>, private val context: Context?, fragment: DoneFragment) : RecyclerView.Adapter<DoneViewHolder>() {
    private val category: SelectedTask
    fun setList(list: List<Task?>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneViewHolder {
        return DoneViewHolder(LayoutInflater.from(context).inflate(R.layout.done_list, parent, false))
    }

    override fun onBindViewHolder(holder: DoneViewHolder, position: Int) {
        holder.title.text = list[position]?.title
        holder.desc.text = list[position]?.minutesWorked
        holder.text.text = list[position]?.title!![0].toString().toUpperCase()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class DoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        val desc: TextView
        private val edit: ImageView
        private val circleImageView: CircleImageView
        val text: TextView

        init {
            title = itemView.findViewById(R.id.done_title)
            desc = itemView.findViewById(R.id.done_desc)
            edit = itemView.findViewById(R.id.done_edit)
            circleImageView = itemView.findViewById(R.id.done_circular)
            text = itemView.findViewById(R.id.done_text)
            itemView.setOnClickListener { v: View? -> category.inform(list[adapterPosition]) }
        }
    }

    init {
        category = fragment
    }
}