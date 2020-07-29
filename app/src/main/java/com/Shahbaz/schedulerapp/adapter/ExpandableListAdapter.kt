package com.Shahbaz.schedulerapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.activities.AddCategory
import com.Shahbaz.schedulerapp.activities.AddTask
import com.Shahbaz.schedulerapp.customTextView.CustomTextView
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Task
import java.util.*

class ExpandableListAdapter(private var listDataGroup: List<Category>?,
                            private var listDataChild: HashMap<Int, List<Task?>?>, private val context: Context?) : BaseExpandableListAdapter() {
    fun setGroup(group: List<Category>?) {
        listDataGroup = group
        notifyDataSetChanged()
    }

    fun setChild(listDataChild: HashMap<Int, List<Task?>?>) {
        this.listDataChild = listDataChild
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return listDataGroup!!.size
    }

    override fun getChildrenCount(i: Int): Int {
        return try {
            listDataChild[listDataGroup!![i].id]!!.size
        } catch (e: NullPointerException) {
            0
        }
    }

    override fun getGroup(i: Int): Any {
        return listDataGroup!![1]
    }

    override fun getChild(i: Int, i1: Int): Any {
        return listDataChild[listDataGroup!![i].id]!![i1]!!
    }

    override fun getGroupId(i: Int): Long {
        return i.toLong()
    }

    override fun getChildId(i: Int, i1: Int): Long {
        return i1.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(i: Int, b: Boolean, view: View, viewGroup: ViewGroup): View {
        var view = view
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.list_view_expandable, null)
        val image: CustomTextView = view.findViewById(R.id.group_image)
        val edit = view.findViewById<ImageView>(R.id.group_edit)
        val title = view.findViewById<TextView>(R.id.group_title)
        image.text = listDataGroup!![i].icon
        view.setBackgroundColor(listDataGroup!![i].color)
        title.text = listDataGroup!![i].name
        edit.setOnClickListener { v: View? ->
            context!!.startActivity(Intent(context, AddCategory::class.java)
                    .putExtra("CategoryClass", listDataGroup!![i]))
        }
        return view
    }

    override fun getChildView(i: Int, i1: Int, b: Boolean, view: View, viewGroup: ViewGroup): View {
        var view = view
        view = LayoutInflater.from(context).inflate(R.layout.list_view_expandable_item, null)
        val title = view.findViewById<TextView>(R.id.item_expand_title)
        val desc = view.findViewById<TextView>(R.id.item_expand_desc)
        val edit = view.findViewById<ImageView>(R.id.item_expand_edit)
        title.text = listDataChild[listDataGroup!![i].id]!![i1]?.title
        val s = "Minute Worked : " + listDataChild[listDataGroup!![i].id]!![i1]?.workingCycle
        desc.text = s
        edit.setOnClickListener { v: View? ->
            context!!.startActivity(Intent(context, AddTask::class.java)
                    .putExtra("TaskClass", listDataChild[listDataGroup!![i].id]!![i1]))
        }
        return view
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return true
    }

    companion object {
        private const val TAG = "ExpandableListAdapter"
    }

}