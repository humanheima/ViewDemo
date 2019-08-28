package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hm.viewdemo.R

class Chapter7Activity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter7Activity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter7)

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnShadowLayerView -> {
                ShadowLayerViewActivity.launch(this)
            }
            R.id.btnTextShadow -> {
                TextShadowActivity.launch(this)
            }
            R.id.btnMaskFilter -> {
                BlurMaskFilterActivity.launch(this)
            }
            R.id.btnBitmapShader -> {
                BitmapShaderActivity.launch(this)
            }
            R.id.btnLinearGradient -> {
                LinearGradientActivity.launch(this)
            }
            R.id.btnRadialGradient -> {
                RadialGradientActivity.launch(this)
            }
        }
    }
}
