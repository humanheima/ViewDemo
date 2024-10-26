package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityUseComposeInFragmentBinding

/**
 * Created by p_dmweidu on 2024/10/21
 * Desc: 测试在Fragment中使用Compose
 */
class UseComposeInFragmentActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, UseComposeInFragmentActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityUseComposeInFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUseComposeInFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAddFragment1.setOnClickListener {

            val fragment = UseComposeInFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
        }

        binding.btnAddFragment2.setOnClickListener {
            val fragment = ExampleFragmentNoXml.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_container, fragment)
                .commit()
        }

    }
}