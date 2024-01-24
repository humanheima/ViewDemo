package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.custom_view.chapter7.widget.LinearGradientView
import com.hm.viewdemo.custom_view.chapter7.widget.PlayPageBackgroundView
import com.hm.viewdemo.widget.LinearGradientView2

class LinearGradientActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, LinearGradientActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var lgView1: LinearGradientView

    private lateinit var playPageBgView1: PlayPageBackgroundView
    private lateinit var playPageBgView2: PlayPageBackgroundView
    private lateinit var playPageBgView3: PlayPageBackgroundView

    private lateinit var lg_view_2: LinearGradientView2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear_gradient)

        lg_view_2 = findViewById(R.id.lg_view_2)

//        lg_view_2.setColors(
//            resources.getColor(R.color.colorAccent),
//            resources.getColor(R.color.colorPrimaryDark)
//        )
        lgView1 = findViewById(R.id.lg_view_1)
        playPageBgView1 = findViewById(R.id.play_page_bg_view1)

        playPageBgView2 = findViewById(R.id.play_page_bg_view2)
        playPageBgView2.setShouldDrawGradientLayer(false)

        playPageBgView3 = findViewById(R.id.play_page_bg_view3)

        playPageBgView3.setShouldDrawAlphaLayer(false)
        playPageBgView2.setShouldDrawGradientLayer(false)


    }
}
