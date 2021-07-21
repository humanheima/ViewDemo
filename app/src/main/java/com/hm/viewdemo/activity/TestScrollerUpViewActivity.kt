package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityTestScrollerUpViewBinding
import com.hm.viewdemo.widget.TestScrollerUpDownView

/**
 * Created by dumingwei on 2020/12/28
 *
 * Desc:
 */
class TestScrollerUpViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestScrollerUpViewBinding

    private lateinit var scrollUpDownView: TestScrollerUpDownView

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

        scrollUpDownView = findViewById(R.id.test_scroller_up_view)

        val mData: MutableList<String> = arrayListOf()

        mData.add("昔闻洞庭水")
        mData.add("今上岳阳楼")
        mData.add("念去去")
        mData.add("千里烟波")
        mData.add("暮霭沉沉楚天阔")
        mData.add("多情自古伤离别")
        mData.add("更哪堪冷落清秋节")

        scrollUpDownView.setData(mData)

        binding.btnStartScrollUp.setOnClickListener {
            scrollUpDownView.smoothScrollUp()

        }
        binding.btnStartScrollDown.setOnClickListener {
            scrollUpDownView.smoothScrollDown()
        }

    }

}