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

/**
 * Crete by dumingwei on 2019-08-01
 * Desc:
 *
 * 参考链接：https://www.jianshu.com/p/b105019028b6
 */
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
    }


}
