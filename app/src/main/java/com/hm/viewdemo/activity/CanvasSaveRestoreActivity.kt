package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2020/6/8
 *
 * Desc: 探索 Canvas 的save  和 restore 相关知识
 */
class CanvasSaveRestoreActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CanvasSaveRestoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_save_restore)
    }


}
