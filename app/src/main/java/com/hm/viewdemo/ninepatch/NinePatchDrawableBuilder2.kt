package com.hm.viewdemo.ninepatch

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.util.DisplayMetrics
import android.util.Log
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by p_dmweidu on 2023/10/23
 * Desc: 9图的构建者
 * 参考链接：
 * 1. https://juejin.cn/post/7188708254346641465
 * 2. https://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651232105&idx=1&sn=fcc4fa956f329f839f2a04793e7dd3b9&mpshare=1&scene=21&srcid=0719Nyt7J8hsr4iYwOjVPXQE#wechat_redirect
 * 3. https://android.googlesource.com/platform/frameworks/base/+/56a2301/include/androidfw/ResourceTypes.h
 *
 */
class NinePatchDrawableBuilder2 {


    companion object {
        const val TAG = "NinePatchDrawableBuilde"
    }

    private var horizontalMirror: Boolean = false // 是否需要做横向的镜像处理
    var density: Int = 160 // 注意：是densityDpi的值，320、480、640等
    private var bitmap: Bitmap? = null
    private var width: Int = 0
    private var height: Int = 0

    private var originWidth: Int = 0
    private var originHeight: Int = 0

    private var resources: Resources? = null


    private var patchRegionHorizontal = mutableListOf<PatchRegionBean2>()
    private var patchRegionVertical = mutableListOf<PatchRegionBean2>()

    private var paddingLeft: Int = 0
    private var paddingRight: Int = 0
    private var paddingTop: Int = 0
    private var paddingBottom: Int = 0


    /**
     * 设置资源文件夹中的图片
     */
    fun setResourceData(
        resources: Resources, resId: Int,
        horizontalMirror: Boolean = false
    ): NinePatchDrawableBuilder2 {
        val currentTimeMillis = System.currentTimeMillis()
        Log.i(TAG, "setResourceData: resId = $resId start at $currentTimeMillis")
        val bitmap: Bitmap? = try {
            BitmapFactory.decodeResource(resources, resId)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
        Log.i(
            TAG,
            "setResourceData: resId = $resId end 耗时 = ${System.currentTimeMillis() - currentTimeMillis} ms  bitmap?.density = ${bitmap?.density}"
        )


        return setBitmapData(
            bitmap = bitmap,
            resources = resources,
            horizontalMirror = horizontalMirror
        )
    }


    /**
     * 设置本地文件夹中的图片
     */
    fun setFileData(
        resources: Resources, file: File,
        horizontalMirror: Boolean = false
    ): NinePatchDrawableBuilder2 {
        val bitmap: Bitmap? = try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
        Log.i(TAG, "setFileData: density = ${bitmap?.density}")

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val density = displayMetrics.density
        val width = (bitmap!!.width * density).toInt()
        val height = (bitmap!!.height * density).toInt()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

//        val matrix = Matrix()
//        matrix.postScale(3f, 3f)
//        val scaledBitmap = Bitmap.createBitmap(
//            bitmap!!,
//            0, 0, bitmap.width, bitmap.height,
//            matrix, true
//        )

        return setBitmapData(
            bitmap = scaledBitmap,
            resources = resources,
            horizontalMirror = horizontalMirror
        )
    }

    /**
     * 设置assets文件夹中的图片
     */
    fun setAssetsData(
        resources: Resources,
        assetFilePath: String,
        horizontalMirror: Boolean = false
    ): NinePatchDrawableBuilder2 {
        var bitmap: Bitmap?

        try {
            val inputStream = resources.assets.open(assetFilePath)
            bitmap = BitmapFactory.decodeStream(inputStream)
            Log.i(TAG, "setAssetsData: bitmap ninePatchChunk = ${bitmap.ninePatchChunk}")
            inputStream.close()
        } catch (e: Throwable) {
            e.printStackTrace()
            bitmap = null
        }

        return setBitmapData(
            bitmap = bitmap,
            resources = resources,
            horizontalMirror = horizontalMirror
        )
    }

    /**
     * 直接处理bitmap数据
     */
    private fun setBitmapData(
        bitmap: Bitmap?, resources: Resources,
        horizontalMirror: Boolean = false
    ): NinePatchDrawableBuilder2 {
        this.bitmap = bitmap
        this.width = bitmap?.width ?: 0
        this.height = bitmap?.height ?: 0

        this.resources = resources
        this.horizontalMirror = horizontalMirror
        return this
    }

    private fun buildChunk(): ByteArray {
        // 横向和竖向端点的数量 = 线段数量 * 2
        val horizontalEndpointsSize = patchRegionHorizontal.size * 2
        val verticalEndpointsSize = patchRegionVertical.size * 2

        val NO_COLOR = 0x00000001
        val COLOR_SIZE = 9 //could change, may be 2 or 6 or 15 - but has no effect on output

        //这里计算的 arraySize 是 int 值，最终占用的字节数是 arraySize * 4
        val arraySize = 1 + 2 + 4 + 1 + horizontalEndpointsSize + verticalEndpointsSize + COLOR_SIZE
        //这里乘以4，是因为一个int占用4个字节
        val byteBuffer = ByteBuffer.allocate(arraySize * 4).order(ByteOrder.nativeOrder())

        byteBuffer.put(1.toByte()) //第一个字节无意义，不等于0就行
        byteBuffer.put(horizontalEndpointsSize.toByte()) //mDivX x数组的长度
        byteBuffer.put(verticalEndpointsSize.toByte()) //mDivY y数组的长度
        byteBuffer.put(COLOR_SIZE.toByte()) //mColor数组的长度

        byteBuffer.putInt(0)//一个int类型的skip，无意义
        byteBuffer.putInt(0)//一个int类型的skip，无意义

        //Note: 等待进一步研究
        // padding 设为0，即使设置了数据，padding依旧可能不生效
        //定于与drawNinePatch的右边和下边的黑线
        byteBuffer.putInt(0)
        byteBuffer.putInt(0)
        byteBuffer.putInt(0)
        byteBuffer.putInt(0)

        byteBuffer.putInt(0)//一个int类型的skip，无意义

        // regions 控制横向拉伸的线段数据
        //mDivX数组
        if (horizontalMirror) {
            //镜像，需要修改横向的拉伸区域
            patchRegionHorizontal.forEach {
                byteBuffer.putInt(width - it.end)
                byteBuffer.putInt(width - it.start)
            }
        } else {
            patchRegionHorizontal.forEach {
                byteBuffer.putInt(it.start * width / 128)
                byteBuffer.putInt(it.end * width / 128)
            }
        }

        //mDivY数组
        // regions 控制竖向拉伸的线段数据
        patchRegionVertical.forEach {
            byteBuffer.putInt(it.start * height / 112)
            byteBuffer.putInt(it.end * height / 112)
        }

        //mColor数组
        for (i in 0 until COLOR_SIZE) {
            byteBuffer.putInt(NO_COLOR)
        }

        return byteBuffer.array()
    }

    fun setPatchHorizontal(vararg patchRegion: PatchRegionBean2): NinePatchDrawableBuilder2 {
        patchRegion.forEach {
            patchRegionHorizontal.add(it)
        }
        return this
    }

    fun setPatchVertical(vararg patchRegion: PatchRegionBean2): NinePatchDrawableBuilder2 {
        patchRegion.forEach {
            patchRegionVertical.add(it)
        }
        return this
    }


    /**
     * 设置原始图片的宽高
     */
    fun setOriginSize(originWidth: Int, originHeight: Int): NinePatchDrawableBuilder2 {
        this.originWidth = originWidth
        this.originHeight = originHeight
        return this
    }

    fun setPadding(
        paddingLeft: Int, paddingRight: Int, paddingTop: Int, paddingBottom: Int,
    ): NinePatchDrawableBuilder2 {
        this.paddingLeft = paddingLeft
        this.paddingRight = paddingRight
        this.paddingTop = paddingTop
        this.paddingBottom = paddingBottom
        return this
    }

    /**
     * 控制内容填充的区域
     * （注意：这里的left，top，right，bottom同xml文件中的padding意思一致，只不过这里是百分比形式）
     */
    private fun buildPadding(): Rect {
        val rect = Rect()
        rect.left = paddingLeft * width / originWidth
        rect.right = (originHeight - paddingRight) * width / originWidth

        rect.top = paddingTop * height / originHeight
        rect.bottom = (originHeight - paddingBottom) * height / originHeight
        return rect
    }

    /**
     * 构造bitmap信息
     * 注意：需要判断是否需要做横向的镜像处理
     */
    private fun buildBitmap(): Bitmap? {
        return if (!horizontalMirror) {
            bitmap
        } else {
            bitmap?.let {
                val matrix = Matrix()
                matrix.setScale(-1f, 1f)
                val newBitmap = Bitmap.createBitmap(
                    it,
                    0, 0, it.width, it.height,
                    matrix, true
                )
                it.recycle()
                newBitmap
            }
        }
    }

    fun getDensityPostfix(res: Resources): String? {
        var result: String? = null
        when (res.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> result = "ldpi"
            DisplayMetrics.DENSITY_MEDIUM -> result = "mdpi"
            DisplayMetrics.DENSITY_HIGH -> result = "hdpi"
            DisplayMetrics.DENSITY_XHIGH -> result = "xhdpi"
            DisplayMetrics.DENSITY_XXHIGH -> result = "xxhdpi"
            DisplayMetrics.DENSITY_XXXHIGH -> result = "xxxhdpi"
        }
        return result
    }

    fun build(): Drawable? {
        return try {
            val buildBitmap = buildBitmap()
            val buildChunk = buildChunk()
            val ninePatchDrawable =
                NinePatchDrawable(resources, buildBitmap, buildChunk, buildPadding(), null)
            //ninePatchDrawable.setTargetDensity(density)
            ninePatchDrawable
        } catch (e: Exception) {
            null
        }
    }


}