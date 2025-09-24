package com.hm.viewdemo.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by p_dmweidu on 2025/9/23
 * Desc:获取View上的 Bitmap
 */
object SaveViewBitmapUtils {

    private const val TAG = "SaveViewBitmapUtils"

    /**
     * 获取指定 View 的 Bitmap
     *
     * @param view 需要转换为 Bitmap 的 View
     * @return 生成的 Bitmap
     */
    fun getViewBitmap(view: View?): Bitmap? {
        if (view == null) {
            return null
        }
        if (view.width == 0 || view.height == 0) {
            return null
        }

        // 创建一个与 View 尺寸相同的 Bitmap
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // 绘制 View 到 Canvas
        view.draw(canvas)
        return bitmap

    }

    /**
     * 保存 Bitmap 到文件，返回文件路径
     */
    fun saveBitmapToFile(context: Context, bitmap: Bitmap?): String? {
        // 示例：保存 Bitmap 到文件
        val dir = File(context.filesDir.path + "pic/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        try {
            val newFile = File(
                dir,
                "image" + System.currentTimeMillis() + ".jpg"
            )
            val out = FileOutputStream(newFile)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            return newFile.absolutePath
        } catch (e: IOException) {
            Log.e(TAG, "saveBitmapToFile error ${e.message}")
            return null
        } finally {
            bitmap?.recycle()
        }
    }

}