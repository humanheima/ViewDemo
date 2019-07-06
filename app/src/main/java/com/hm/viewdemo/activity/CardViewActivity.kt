package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_card_view.*


class CardViewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view)
        tvText.text = getTextSpan()
        tvText.movementMethod = LinkMovementMethod.getInstance()
    }


    private fun getTextSpan(): SpannableString {
        val text = "如定位不准，请尝试刷新定位"
        val spanText = SpannableString(text)
        spanText.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (widget is TextView) {
                    widget.highlightColor = Color.TRANSPARENT
                }
                Toast.makeText(this@CardViewActivity, "haha", Toast.LENGTH_SHORT).show()
                //manuallyRefresh()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(R.color.colorPrimary)
                ds.isUnderlineText = false
            }
        }, 9, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        return spanText
    }
}
