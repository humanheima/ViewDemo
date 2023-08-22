package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.AnimatedGradientDrawable

/**
 * Crete by dumingwei on 2019-08-12
 * Desc: 学习Path的基本操作
 * 参考: https://www.gcssloop.com/customview/Path_Basic/
 *
 */
class DrawPathActivity : AppCompatActivity() {


    private var ivAnimatorGradientDrawable: ImageView? = null

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, DrawPathActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_path)
        ivAnimatorGradientDrawable = findViewById(R.id.iv_animator_gradient_drawable)
        val animatedGradientDrawable = AnimatedGradientDrawable()
        animatedGradientDrawable.cornerRadius = ScreenUtil.dpToPx(this, 10)
        ivAnimatorGradientDrawable?.setImageDrawable(animatedGradientDrawable)

    }
}
