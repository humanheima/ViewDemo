package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_bitmap_shader.*

class BitmapShaderActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, BitmapShaderActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val TAG = "BitmapShaderActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap_shader)
        view5.post {
            Log.i(TAG, "onCreate:${view5.width}, ${view5.height}")
        }
    }
}
