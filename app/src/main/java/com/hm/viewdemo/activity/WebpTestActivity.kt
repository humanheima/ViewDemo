package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.webp.decoder.WebpDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-10-11
 * Desc:
 *
 */
class WebpTestActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_webp_test)
        //ivWebP.background = resources.getDrawable(R.mipmap.ballon_another)
//        Glide.with(this)
//            .load("https://www.gstatic.com/webp/animated/1.webp")
//            .addListener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?, model: Any?,
//                    target: Target<Drawable>, isFirstResource: Boolean
//                ): Boolean {
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable, model: Any,
//                    target: Target<Drawable>, dataSource: DataSource,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    if (resource is WebpDrawable) {
//                        ivWebP.setImageDrawable(resource)
//                        resource.startFromFirstFrame()
//                    }
//                    return true
//                }
//            }).preload()

    }
}
