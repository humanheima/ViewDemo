package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2022/6/12
 * Desc: 测试资源加载
 */
class LoadResActivity : AppCompatActivity() {


    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView
    private lateinit var iv4: ImageView

    companion object {

        private const val TAG = "LoadResActivity"

        fun launch(context: Context) {
            val starter = Intent(context, LoadResActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_res)

        val color = resources.getColor(R.color.colorPrimary)
        val color2 = resources.getColor(R.color.color_list)
        val assetManager = assets

        Log.i(TAG, "onCreate: assetManager = $assetManager")

        iv1 = findViewById(R.id.iv_1)
        iv2 = findViewById(R.id.iv_2)
        iv3 = findViewById(R.id.iv_3)
        iv4 = findViewById(R.id.iv_4)

        iv1.background =resources.getDrawable(R.drawable.avatar)

        iv1.post {
            Log.i(TAG, "onCreate: ${iv1.background}")
            Log.i(TAG, "onCreate: ${iv2.background}")
            Log.i(TAG, "onCreate: ${iv3.background}")
            Log.i(TAG, "onCreate: ${iv4.background}")
        }

    }
}