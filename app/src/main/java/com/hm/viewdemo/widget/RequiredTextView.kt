package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-09-17.
 * Desc:
 */
class RequiredTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private val TAG = "RequiredTextView"


    companion object {

        const val PREFIX = ""
        const val POSTFIX = ""

    }

    private var requiredText = ""
    private var prefix = PREFIX
    private var postfix = POSTFIX

    private var prefixColor = Color.BLACK

    private var postfixColor = Color.BLACK

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RequiredTextView)
        requiredText = ta.getString(R.styleable.RequiredTextView_required_text) ?: ""
        prefix = ta.getString(R.styleable.RequiredTextView_prefix) ?: PREFIX
        postfix = ta.getString(R.styleable.RequiredTextView_postfix) ?: POSTFIX

        prefixColor = ta.getInt(R.styleable.RequiredTextView_prefix_color, Color.BLACK)
        postfixColor = ta.getInt(R.styleable.RequiredTextView_postfix_color, Color.BLACK)

        val dimension = ta.getDimension(R.styleable.RequiredTextView_postfix_text_size, 0f)
        val dimensionPixelSize = ta.getDimensionPixelSize(R.styleable.RequiredTextView_postfix_text_size, 0)
        val dimensionPixelOffset = ta.getDimensionPixelOffset(R.styleable.RequiredTextView_postfix_text_size, 0)

        Log.d(TAG, "dimension = $dimension ")
        Log.d(TAG, "dimensionPixelSize = $dimensionPixelSize ")
        Log.d(TAG, "dimensionPixelOffset = $dimensionPixelOffset ")

        ta.recycle()

        val builder = SpannableStringBuilder()

        builder.append(prefix)
        builder.append(requiredText)
        builder.append(postfix)

        if (prefix.isNotEmpty()) {
            val colorSpan = ForegroundColorSpan(prefixColor)
            builder.setSpan(colorSpan, 0, prefix.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (postfix.isNotEmpty()) {
            val colorSpan = ForegroundColorSpan(postfixColor)
            builder.setSpan(colorSpan, builder.length - postfix.length, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        text = builder
    }

    /*override fun setText(text: CharSequence, type: BufferType?) {

        val builder = SpannableStringBuilder()
        val colorSpan = ForegroundColorSpan(postfixColor)

        when (direction) {
            DIRECTION_START -> {
                builder.append(requiredText)
                builder.append(text)
                builder.setSpan(colorSpan, 0, requiredText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            DIRECTION_END -> {
                builder.append(text)
                builder.append(requiredText)
                builder.setSpan(colorSpan, text.length, requiredText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        super.setText(builder, type)
    }*/
}