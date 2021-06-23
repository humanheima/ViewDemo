package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.custom_view.chapter7.widget.PlayPageBackgroundView

class LinearGradientActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LinearGradientActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var playPageBgView1: PlayPageBackgroundView
    private lateinit var playPageBgView2: PlayPageBackgroundView
    private lateinit var playPageBgView3: PlayPageBackgroundView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_gradient)

        playPageBgView1 = findViewById(R.id.play_page_bg_view1)
        playPageBgView2 = findViewById(R.id.play_page_bg_view2)
        playPageBgView2.setShouldDrawGradientLayer(false)

        playPageBgView3 = findViewById(R.id.play_page_bg_view3)

        playPageBgView3.setShouldDrawAlphaLayer(false)
        playPageBgView2.setShouldDrawGradientLayer(false)

    }
}
