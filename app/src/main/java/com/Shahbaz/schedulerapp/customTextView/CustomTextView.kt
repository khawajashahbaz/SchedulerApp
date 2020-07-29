package com.Shahbaz.schedulerapp.customTextView

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView : AppCompatTextView {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init() {
        val tf = Typeface.createFromAsset(context.assets, "icomoon.ttf")
        typeface = tf
    }
}