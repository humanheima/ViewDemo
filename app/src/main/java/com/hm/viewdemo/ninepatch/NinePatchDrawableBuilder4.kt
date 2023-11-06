package com.hm.viewdemo.ninepatch

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
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
 * 4. 第一次加载图片的时候，从文件解析出Bitmap，然后存储在缓存里，下次再加载的时候，直接从缓存拿
 * @see com.yuewen.dreamer.bubble.BitmapLruCache
 *
 * 注意：一组图片的宽高必须一致，不然可能会导致未知bug
 * 注意：一组图片的宽高必须一致，不然可能会导致未知bug
 * 注意：一组图片的宽高必须一致，不然可能会导致未知bug
 */
class NinePatchDrawableBuilder4 {

    companion object {
        const val TAG = "NinePatchDrawableBuild4"

        //Note: 把加载过的图片缓存在内存里
        private val bitmapLruCache = BitmapLruCache()

        val NO_COLOR = 0x00000001
        val COLOR_SIZE = 9

    }

    private var bitmap: Bitmap? = null
    private var width: Int = 0
    private var height: Int = 0

    private var originWidth: Int = 0
    private var originHeight: Int = 0

    private var resources: Resources? = null

    private var patchRegionHorizontal = mutableListOf<PatchStretchBean>()
    private var patchRegionVertical = mutableListOf<PatchStretchBean>()

    private var paddingLeft: Int = 0
    private var paddingRight: Int = 0
    private var paddingTop: Int = 0
    private var paddingBottom: Int = 0


    /**
     * 设置本地文件夹中的图片
     * @param dir 本地文件夹
     * 注意，资源都是一倍图，从资源加载会自动缩放到当前的density。如果是从文件加载，则需要自己处理缩放。
     */
    fun getAnimationDrawableFromResource(
        resources: Resources,
        resIdList: MutableList<Int>,
        patchHorizontal: PatchStretchBean,
        patchVertical: PatchStretchBean,
        paddingRect: Rect,
        originWidth: Int,
        originHeight: Int,
    ): AnimationDrawable? {

        val currentTimeMillis = System.currentTimeMillis()

        if (resIdList.isNullOrEmpty()) {
            return null
        }

        setPatchHorizontal(patchHorizontal)
        setPatchVertical(patchVertical)
        setPadding(
            paddingLeft = paddingRect.left,
            paddingTop = paddingRect.top,
            paddingRight = paddingRect.right,
            paddingBottom = paddingRect.bottom
        )
        setOriginSize(originWidth, originHeight)
        val animationDrawable = CanStopAnimationDrawable()
        //设置循环10次，就结束
        animationDrawable.setFinishCount(100)

        resIdList.forEach { resId ->
            val ninePatchDrawable = setResourceData(resources, resId)
                .build()
            if (ninePatchDrawable != null) {
                animationDrawable.addFrame(ninePatchDrawable, 100)
            }
        }
        animationDrawable.isOneShot = false


        Log.i(
            TAG,
            "getAnimationDrawableFromResource: end 耗时：${System.currentTimeMillis() - currentTimeMillis} ms"
        )
        return animationDrawable

    }


    /**
     * 设置资源文件夹中的图片
     */
    private fun setResourceData(resources: Resources, resId: Int): NinePatchDrawableBuilder4 {
        val resIdString = resId.toString()
        Log.i(TAG, "setResourceData: resId = $resId")
        var bitmap = bitmapLruCache.getBitmap(resIdString)

        if (bitmap == null) {
            bitmap = try {
                BitmapFactory.decodeResource(resources, resId)
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }

            if (bitmap != null) {
                Log.i(TAG, "setResourceData: width = ${bitmap.width}, height = ${bitmap.height}")
//                // warning：2023/11/5: 注意，这里从文件里加载的是1倍图，所以要放大一下
//                //从文件加载，需要处理缩放，如果文件里是1倍图，需要放大一下
//                val displayMetrics: DisplayMetrics = resources.displayMetrics
//                val density = displayMetrics.density
//
//                val width = (bitmap.width * density).toInt()
//                val height = (bitmap.height * density).toInt()
//
//                Log.i(TAG, "setFileData: width = ${bitmap.width}, height = ${bitmap.height}")
//                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
//                Log.i(
//                    TAG,
//                    "setFileData: scaledBitmap width = ${scaledBitmap.width}, height = ${scaledBitmap.height}"
//                )
//                bitmapLruCache.putBitmap(absolutePath, scaledBitmap)
//                bitmap = scaledBitmap
                bitmapLruCache.putBitmap(resIdString, bitmap)
            }
        } else {
            Log.i(TAG, "setResourceData: 从缓存中获取bitmap != null")
        }

        // warning：2023/11/5: 注意，这里从文件里加载的是1倍图

//        val matrix = Matrix()
//        matrix.postScale(3f, 3f)
//        val scaledBitmap = Bitmap.createBitmap(
//            bitmap!!,
//            0, 0, bitmap.width, bitmap.height,
//            matrix, true
//        )

        return setBitmapData(
            bitmap = bitmap,
            resources = resources
        )
    }


    /**
     * 设置本地文件夹中的图片
     * @param dir 本地文件夹
     */
    fun getAnimationDrawableFromFile(
        context: Context,
        resources: Resources,
        dir: File,
        patchHorizontal: PatchStretchBean,
        patchVertical: PatchStretchBean,
        paddingRect: Rect,
        originWidth: Int,
        originHeight: Int,
    ): AnimationDrawable? {

        val currentTimeMillis = System.currentTimeMillis()

        if (!dir.exists()) {
            return null
        }
        val files = dir.listFiles()
        if (files.isNullOrEmpty()) {
            return null
        }

        setPatchHorizontal(patchHorizontal)
        setPatchVertical(patchVertical)
        setPadding(
            paddingLeft = paddingRect.left,
            paddingTop = paddingRect.top,
            paddingRight = paddingRect.right,
            paddingBottom = paddingRect.bottom
        )
        setOriginSize(originWidth, originHeight)
        val animationDrawable = CanStopAnimationDrawable()
        //设置循环10次，就结束
        animationDrawable.setFinishCount(100)
        files.forEach { pngFile ->
            val ninePatchDrawable = setFileData(context, resources, pngFile)
                .build()
            if (ninePatchDrawable != null) {
                animationDrawable.addFrame(ninePatchDrawable, 100)
            }
        }
        animationDrawable.isOneShot = false



        Log.i(
            TAG,
            "getAnimationDrawableFromFile: end 耗时：${System.currentTimeMillis() - currentTimeMillis} ms"
        )
        return animationDrawable

    }

    /**
     * 设置本地文件夹中的图片
     * 从文件解析的话，需要处理缩放，density
     * @param file 本地png文件路径
     */
    private fun setFileData(
        context: Context,
        resources: Resources,
        file: File
    ): NinePatchDrawableBuilder4 {

        val absolutePath = file.absolutePath
        var bitmap = bitmapLruCache.getBitmap(absolutePath)

        if (bitmap == null) {
            bitmap = try {
                BitmapFactory.decodeFile(absolutePath)
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }


            if (bitmap != null) {

                // warning：2023/11/5: 注意，这里从文件里加载的是1倍图，所以要放大一下
                //从文件加载，需要处理缩放，如果文件里是1倍图，需要放大一下
                val displayMetrics: DisplayMetrics = resources.displayMetrics
                val density = displayMetrics.density

                val width = (bitmap.width * density).toInt()
                val height = (bitmap.height * density).toInt()

                Log.i(TAG, "setFileData: width = ${bitmap.width}, height = ${bitmap.height}")
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
                Log.i(
                    TAG,
                    "setFileData: scaledBitmap width = ${scaledBitmap.width}, height = ${scaledBitmap.height}"
                )
                bitmapLruCache.putBitmap(absolutePath, scaledBitmap)
                bitmap = scaledBitmap
            }
        }


//        val matrix = Matrix()
//        matrix.postScale(3f, 3f)
//        val scaledBitmap = Bitmap.createBitmap(
//            bitmap!!,
//            0, 0, bitmap.width, bitmap.height,
//            matrix, true
//        )

        return setBitmapData(
            bitmap = bitmap,
            resources = resources
        )
    }

    /**
     * 直接处理bitmap数据
     */
    private fun setBitmapData(
        bitmap: Bitmap?, resources: Resources
    ): NinePatchDrawableBuilder4 {
        this.bitmap = bitmap
        this.width = bitmap?.width ?: 0
        this.height = bitmap?.height ?: 0
        this.resources = resources
        return this
    }

    private fun buildChunk(): ByteArray {
        // 横向和竖向端点的数量 = 线段数量 * 2
        val horizontalEndpointsSize = patchRegionHorizontal.size * 2
        val verticalEndpointsSize = patchRegionVertical.size * 2

        //这里计算的 arraySize 是 int 值，最终占用的字节数是 arraySize * 4
        val arraySize = 1 + 2 + 4 + 1 + horizontalEndpointsSize + verticalEndpointsSize + COLOR_SIZE
        //这里乘以4，是因为一个int占用4个字节
        val byteBuffer = ByteBuffer.allocate(arraySize * 4).order(ByteOrder.nativeOrder())

        byteBuffer.put(1.toByte()) //第一个字节无意义，不等于0就行
        byteBuffer.put(horizontalEndpointsSize.toByte()) //mDivX x数组的长度
        byteBuffer.put(verticalEndpointsSize.toByte()) //mDivY y数组的长度
        byteBuffer.put(COLOR_SIZE.toByte()) //mColor数组的长度

        // skip 8 bytes
        byteBuffer.putInt(0)
        byteBuffer.putInt(0)

        //Note: 如果这里不加的话，拉伸会有问题。目前还没搞清楚
        //左右padding
        byteBuffer.putInt(mRectPadding.left)
        byteBuffer.putInt(mRectPadding.right)
        //上下padding
        byteBuffer.putInt(mRectPadding.top)
        byteBuffer.putInt(mRectPadding.bottom)

//        byteBuffer.putInt(0)
//        byteBuffer.putInt(0)
//        //上下padding
//        byteBuffer.putInt(0)
//        byteBuffer.putInt(0)


//        // skip 4 bytes
        byteBuffer.putInt(0)

        // regions 控制横向拉伸的线段数据
        //mDivX数组
        patchRegionHorizontal.forEach {
            byteBuffer.putInt(it.start * width / originWidth)
            byteBuffer.putInt(it.end * width / originWidth)
        }

        //mDivY数组
        // regions 控制竖向拉伸的线段数据
        patchRegionVertical.forEach {
            byteBuffer.putInt(it.start * height / originHeight)
            byteBuffer.putInt(it.end * height / originHeight)
        }

        //mColor数组
        for (i in 0 until COLOR_SIZE) {
            byteBuffer.putInt(NO_COLOR)
        }

        return byteBuffer.array()
    }

    private fun setPatchHorizontal(vararg patchRegion: PatchStretchBean) {
        patchRegion.forEach {
            if (patchRegionHorizontal.contains(it)) {
                return@forEach
            }
            patchRegionHorizontal.add(it)
        }
    }

    private fun setPatchVertical(vararg patchRegion: PatchStretchBean) {
        patchRegion.forEach {
            if (patchRegionVertical.contains(it)) {
                return@forEach
            }
            patchRegionVertical.add(it)
        }
    }


    /**
     * 设置原始图片的宽高
     */
    private fun setOriginSize(originWidth: Int, originHeight: Int): NinePatchDrawableBuilder4 {
        this.originWidth = originWidth
        this.originHeight = originHeight
        return this
    }

    private fun setPadding(
        paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int,
    ) {
        this.paddingLeft = paddingLeft
        this.paddingTop = paddingTop
        this.paddingRight = paddingRight
        this.paddingBottom = paddingBottom
    }

    private var mRectPadding = Rect()

    /**
     * 控制内容填充的区域
     * （注意：这里的left，top，right，bottom同xml文件中的padding意思一致，只不过这里是百分比形式）
     */
    private fun buildPadding() {
        mRectPadding.left = ((paddingLeft * width) / originWidth)
        mRectPadding.right = ((originWidth - paddingRight) * width / originWidth)
        mRectPadding.top = (paddingTop * height / originHeight)
        mRectPadding.bottom = ((originHeight - paddingBottom) * height / originHeight)

        Log.i(TAG, "buildPadding: rect = $mRectPadding")
    }

    /**
     * 构造bitmap信息
     * 注意：需要判断是否需要做横向的镜像处理
     */
    private fun buildBitmap(): Bitmap? {
        return bitmap
    }

    private fun build(): Drawable? {
        return try {
            val buildBitmap = buildBitmap()
            if (mRectPadding.left == 0 && mRectPadding.right == 0 && mRectPadding.top == 0 && mRectPadding.bottom == 0) {
                buildPadding()
            }
            val buildChunk = buildChunk()
            val ninePatchDrawable =
                NinePatchDrawable(resources, buildBitmap, buildChunk, mRectPadding, null)
            ninePatchDrawable
        } catch (e: Exception) {
            null
        }
    }


}