package com.dicoding.smartbin.ui

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.smartbin.R

class button_login: AppCompatButton {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = ContextCompat.getDrawable(context, R.drawable.button_login)
        hint = context.getString(R.string.login)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        TODO("Not yet implemented")
    }
}