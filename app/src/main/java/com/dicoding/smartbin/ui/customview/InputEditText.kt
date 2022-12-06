package com.dicoding.smartbin.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.smartbin.R

class InputEditText : AppCompatEditText {
    private lateinit var icon: Drawable

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

        icon = when (hint) {
            context.getString(R.string.hint_username) -> {
                ContextCompat.getDrawable(context, R.drawable.ic_person) as Drawable
            }
            context.getString(R.string.hint_email) -> {
                ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
            }
            context.getString(R.string.hint_komplek) -> {
                ContextCompat.getDrawable(context, R.drawable.ic_home) as Drawable
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.ic_list) as Drawable
            }
        }

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
            }

            override fun afterTextChanged(s: Editable) {
                if (hint == context.getString(R.string.hint_email)){
                    if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        error = context.getString(R.string.error_email)
                    }
                }
            }
        })
    }

    private fun showIcon() {
        setButtonDrawables(startOfTheText = icon)
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
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}