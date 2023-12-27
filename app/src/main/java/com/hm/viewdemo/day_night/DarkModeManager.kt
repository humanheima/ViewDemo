package com.hm.viewdemo.day_night

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.view.drawToBitmap
import com.hm.viewdemo.activity.MainActivity


/**
 * Created by p_dmweidu on 2023/12/27
 * Desc:
 */
object DarkModeManager {

    private val TAG = "DarkModeMaskActivity"
    var screenShot: Bitmap? = null

    /**
     * 启动遮罩页
     */
    fun start(activity: Activity?) {
        if (activity == null) return
        //screenShot = activity.window.decorView.drawToBitmap()

        Log.i(TAG, "[start] invoked.")
        val intent = Intent(activity, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.overridePendingTransition(0, 0)
    }


}