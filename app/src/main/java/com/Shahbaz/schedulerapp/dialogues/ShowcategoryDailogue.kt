package com.Shahbaz.schedulerapp.dialogues

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Shahbaz.schedulerapp.R
import com.Shahbaz.schedulerapp.adapter.CategoryAdapter
import com.Shahbaz.schedulerapp.databaseUitlity.entities.Category
import com.Shahbaz.schedulerapp.viewModel.SchedulerViewModel
import java.util.*

class ShowcategoryDailogue : DialogFragment {
    private var observer: Observer<String>? = null

    constructor() {}
    constructor(observer: Observer<String>?) {
        this.observer = observer
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        val view = LayoutInflater.from(context).inflate(R.layout.category_dialogue, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.category_rec_view)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(false)
        val adapter = CategoryAdapter(ArrayList(), context)
        val viewModel = ViewModelProviders.of(this).get(SchedulerViewModel::class.java)
        viewModel.categoryLiveData?.observe(this, Observer { categories: List<Category?>? -> adapter.setList(categories) })
        recyclerView.adapter = adapter
        builder.setView(view)
        // Create the AlertDialog object and return it
        return builder.create()
    }
}