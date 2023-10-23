package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.ninepatch.NinePatchDrawableBuilder
import com.hm.viewdemo.ninepatch.PatchRegionBean

/**
 * Created by p_dmweidu on 2023/10/22
 * Desc: NinePatchDrawable 测试点9图的使用
 */
class NinePatchDrawableActivity : AppCompatActivity() {


    private var ivNinepatch1: ImageView? = null

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, NinePatchDrawableActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nine_patch_drawable)

        ivNinepatch1 = findViewById(R.id.iv_ninepatch_1)

        val drawable =
            NinePatchDrawableBuilder().setResourceData(
                resources,
                R.drawable.ic_dog,
                false
            )
                .setPatchHorizontal(PatchRegionBean(0.2f, 0.3f))
                .setPatchVertical(PatchRegionBean(0.8f, 0.9f))
                .setPadding(0.1f, 0.1f, 0.1f, 0.1f)
                .build()
        ivNinepatch1?.background = drawable


    }
}