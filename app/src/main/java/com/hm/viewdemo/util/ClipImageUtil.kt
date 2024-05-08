package com.hm.viewdemo.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

class ClipImageUtil {

    /**
     * 实现一个算法，把传入的src图片，高度剪裁为1920，然后保存到文件
     */
    fun clipImage(resource: Resources, srcId: Int, dest: String) {

        // 1. 读取src图片
        val bitmap = BitmapFactory.decodeResource(resource, srcId)

        // 2. 获取src图片的宽高
        val srcWidth = bitmap.width
        val srcHeight = bitmap.height

        // 3. 计算剪裁后的宽高
        val destWidth = srcWidth
        val destHeight = 1920

        // 4. 创建一个新的bitmap，用于保存剪裁后的图片
        val destBitmap = Bitmap.createBitmap(bitmap, 0, 0, destWidth, destHeight)

        // 5. 保存dest图片
        val destFile = File(dest)
        destBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(destFile))

        // 6. 释放资源
        bitmap.recycle()
        destBitmap.recycle()
    }


}