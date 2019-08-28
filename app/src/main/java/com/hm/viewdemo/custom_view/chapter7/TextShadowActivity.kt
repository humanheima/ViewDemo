package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_text_shadow.*

class TextShadowActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TextShadowActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_shadow)

        //通过代码给文字设置阴影
        tv.setShadowLayer(5f, 10f, 10f, resources.getColor(R.color.colorPrimary))
    }
}
