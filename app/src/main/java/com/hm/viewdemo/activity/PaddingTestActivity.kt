package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityPaddingTestBinding
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_vertical_vertical.view.*

/**
 * Created by dumingwei on 2021/3/18
 *
 * Desc: 测试padding相关
 */
class PaddingTestActivity : AppCompatActivity() {

    private val TAG: String = "PaddingTestActivity"

    private lateinit var binding: ActivityPaddingTestBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PaddingTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaddingTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val headLayout = LayoutInflater.from(this).inflate(R.layout.head_of_test_padding, null)
        //val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        //binding.llRoot.addView(headLayout, 0, layoutParams)
        //binding.llRoot.setPadding(0, ScreenUtil.dpToPx(this, -200), 0, 0)

    }
}