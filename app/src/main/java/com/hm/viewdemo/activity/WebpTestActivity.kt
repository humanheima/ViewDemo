package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityWebpTestBinding

/**
 * Crete by dumingwei on 2019-10-11
 * Desc:
 *
 */
class WebpTestActivity : BaseActivity<ActivityWebpTestBinding>() {

    private var ivWebp: ImageView? = null

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, WebpTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.imageView.background = resources.getDrawable(R.mipmap.ballon_another)
    }

    override fun createViewBinding(): ActivityWebpTestBinding {
        return ActivityWebpTestBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.activityPosition.setImageResource(R.drawable.test_palette2)
        binding.btnPreload.setOnClickListener {
            testPreload()
        }
    }

    private fun testPreload() {
        Glide.with(this)
            .load("https://www.gstatic.com/webp/animated/1.webp")
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is WebpDrawable) {
                        binding.imageView.setImageDrawable(resource)
                        resource.startFromFirstFrame()
                    }
                    return true
                }
            }).preload()
    }



}
