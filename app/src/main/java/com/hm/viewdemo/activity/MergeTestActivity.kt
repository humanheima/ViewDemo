package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R


/**
 * Created by p_dmweidu on 2023/10/17
 * Desc: 测试merge，inlcude的时候设置id等问题
 * https://cloud.tencent.com/developer/article/1476470
 */
class MergeTestActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, MergeTestActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge_test)

        val tvInclude1 = findViewById<TextView>(R.id.tv_include1)
        tvInclude1.text = "tv_include1"

        /**
         * 两个id R.id.tv_same 一样的TextView
         */
        val tvSame = findViewById<TextView>(R.id.tv_same)
        tvSame.text = "1.3 这里的TextView的ID是tv_same"
        val viewSame = findViewById<FrameLayout>(R.id.view_same)
        val tvSame2 = viewSame.findViewById<TextView>(R.id.tv_same)
        tvSame2.text = "1.3 这里的TextView的ID也是tv_same"
    }
}