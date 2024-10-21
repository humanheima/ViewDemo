package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hm.viewdemo.R

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_use_compose_in_fragment)
        val fragment = UseComposeInFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_container, fragment)
            .commit()
    }
}