package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityRunningTextViewDemoBinding
import com.hm.viewdemo.widget.scrollnumber.NumberRunningTextView

class RunningTextViewDemoActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, RunningTextViewDemoActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityRunningTextViewDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRunningTextViewDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvMoney.setContent("1454.00")
        binding.tvNum.setContent("300")


        binding.btnStart.setOnClickListener {
            binding.tvMoney.setContent("14540000.00")
            binding.tvNum.setContent("284")
        }
    }


}
