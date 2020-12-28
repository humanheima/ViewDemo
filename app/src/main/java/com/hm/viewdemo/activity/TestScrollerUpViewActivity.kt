package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityTestScrollerUpViewBinding
import com.hm.viewdemo.widget.TestScrollerUpView

/**
 * Created by dumingwei on 2020/12/28
 *
 * Desc:
 */
class TestScrollerUpViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestScrollerUpViewBinding

    private lateinit var scrollUpView: TestScrollerUpView

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TestScrollerUpViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScrollerUpViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scrollUpView = findViewById(R.id.test_scroller_up_view)

        binding.btnStartScrollUp.setOnClickListener {
            scrollUpView.smoothScrollUp()

        }
        binding.btnStartScrollDown.setOnClickListener {
            scrollUpView.smoothScrollDown()
        }

    }

}