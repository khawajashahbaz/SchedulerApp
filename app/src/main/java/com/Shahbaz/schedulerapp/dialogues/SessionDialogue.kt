package com.Shahbaz.schedulerapp.dialogues

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

class SessionDialogue : DialogFragment {
    private var observer: Observer<Int>? = null

    constructor() {}
    constructor(observer: Observer<Int>?) {
        this.observer = observer
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(activity!!)
        val editText = EditText(context)
        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(editText)
        builder.setMessage("Enter the number")
                .setPositiveButton("Ok") { dialog: DialogInterface?, id: Int ->
                    if (!editText.text.toString().trim { it <= ' ' }.isEmpty()) {
                        observer!!.onChanged(Integer.valueOf(editText.text.toString().trim { it <= ' ' }))
                    } else {
                        editText.error = "Please fill the number"
                    }
                }
                .setNegativeButton("cancel") { dialog: DialogInterface?, id: Int -> }
        // Create the AlertDialog object and return it
        return builder.create()
    }
}