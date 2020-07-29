package com.Shahbaz.schedulerapp.dialogues

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.Shahbaz.schedulerapp.R

class ShowTimeDialogue : DialogFragment {
    private var observer: Observer<String>? = null

    constructor() {}

    private var minutes: String? = null
    private var seconds: String? = null

    constructor(observer: Observer<String>?) {
        this.observer = observer
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        val view = LayoutInflater.from(context).inflate(R.layout.time_dialogue, null)
        val minute = view.findViewById<EditText>(R.id.dialogue_minute)
        val second = view.findViewById<EditText>(R.id.dialogue_seconds)
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialog: DialogInterface?, id: Int ->
            if (performCheck(minute, second)) {
                observer!!.onChanged("$minutes:$seconds")
            } else {
                minute.error = "Please enter minutes"
            }
        }.setNegativeButton("cancel") { dialog: DialogInterface?, id: Int -> }
        // Create the AlertDialog object and return it
        return builder.create()
    }

    private fun performCheck(minute: EditText, second: EditText): Boolean {
        if (minute.text.toString().trim { it <= ' ' }.isEmpty()) return false
        seconds = if (second.text.toString().trim { it <= ' ' }.isEmpty()) {
            "00"
        } else {
            "" + Integer.valueOf(second.text.toString().trim { it <= ' ' }) % 60
        }
        minutes = minute.text.toString()
        return true
    }
}