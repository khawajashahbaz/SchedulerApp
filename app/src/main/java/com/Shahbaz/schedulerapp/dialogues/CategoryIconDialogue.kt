package com.Shahbaz.schedulerapp.dialogues

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.CategoryIconAdapter
import java.util.*

class CategoryIconDialogue : DialogFragment() {
    private val observer: Observer<String>? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        val view = LayoutInflater.from(context).inflate(R.layout.category_icon_dialog, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.category_icon_rec)
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.setHasFixedSize(false)
        val list: MutableList<Int> = ArrayList()
        for (i in 32..137) list.add(i)
        for (i in 145..154) list.add(i)
        val adapter = CategoryIconAdapter(context, list)
        recyclerView.adapter = adapter
        builder.setView(view)

        // Create the AlertDialog object and return it
        return builder.create()
    }
}