package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_webp_test.*

/**
 * Crete by dumingwei on 2019-10-11
 * Desc:
 *
 */
class WebpTestActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, WebpTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webp_test)
        ivWebP.background = resources.getDrawable(R.mipmap.ballon_another)
    }
}
