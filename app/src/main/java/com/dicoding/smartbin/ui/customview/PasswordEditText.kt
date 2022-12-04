package com.dicoding.smartbin.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.smartbin.R

class PasswordEditText : AppCompatEditText {
    private lateinit var lockIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        lockIcon = ContextCompat.getDrawable(context, R.drawable.ic_password) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (s.toString().isEmpty()) {
                    error = context.getString(R.string.error_empty)
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    error = context.getString(R.string.error_empty)
                }
                if (s.toString().length < 6) {
                    error = context.getString(R.string.pass_requirement)
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun showIcon() {
        setButtonDrawables(startOfTheText = lockIcon)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,topOfTheText,endOfTheText,bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = ContextCompat.getDrawable(context, R.drawable.textbox_view)
        showIcon()
        hint = context.getString(R.string.hint_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}