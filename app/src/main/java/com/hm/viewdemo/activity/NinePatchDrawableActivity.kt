package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.ninepatch.NinePatchDrawableBuilder2
import com.hm.viewdemo.ninepatch.PatchRegionBean2
import java.io.File


/**
 * Created by p_dmweidu on 2023/10/22
 * Desc: NinePatchDrawable 测试点9图的使用
 */
class NinePatchDrawableActivity : AppCompatActivity() {


    private var tv9Patch1: TextView? = null
    private var tv9Patch2: TextView? = null
    private var tv9Patch3: TextView? = null
    private var tv9Patch4: TextView? = null

    private var rl9Patch4: RelativeLayout? = null

    private var resIdList: MutableList<Int> = mutableListOf()
    private var resIdList2: MutableList<Int> = mutableListOf()
    private var bitmapList: MutableList<Bitmap> = mutableListOf()

    companion object {

        private const val TAG = "NinePatchDrawableActivi"

        fun launch(context: Context) {
            val starter = Intent(context, NinePatchDrawableActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nine_patch_drawable)

        tv9Patch1 = findViewById(R.id.tv_9_patch1)
        tv9Patch2 = findViewById(R.id.tv_9_patch2)
        tv9Patch3 = findViewById(R.id.tv_9_patch3)
        tv9Patch4 = findViewById(R.id.tv_9_patch4)

        rl9Patch4 = findViewById(R.id.rl_9_patch4)

//        val drawable1 =
//            NinePatchDrawableBuilder().setResourceData(
//                resources,
//                R.drawable.bubble_frame1,
//                false
//            )
//                .setPatchHorizontal(PatchRegionBean(0.43f, 0.468f))
//                .setPatchVertical(PatchRegionBean(0.46f, 0.464f))
//                //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
//                //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
//                .setPadding(0.234f, 0.258f, 0.42f, 0.40f)
//                .build()
//        tv9Patch1?.background = drawable1


//        val drawable2 =
//            NinePatchDrawableBuilder().setResourceData(
//                resources,
//                R.drawable.bubble_frame1,
//                false
//            )
//                .setPatchHorizontal(PatchRegionBean(0.43f, 0.47f))
//                .setPatchVertical(PatchRegionBean(0.46f, 0.5f))
//                //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
//                //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
//                .setPadding(0.234f, 0.258f, 0.42f, 0.40f)
//                .build()
//        tv9Patch2?.background = drawable2


        resIdList.add(R.drawable.bubble_frame1)
        resIdList.add(R.drawable.bubble_frame2)
        resIdList.add(R.drawable.bubble_frame3)
        resIdList.add(R.drawable.bubble_frame4)
        resIdList.add(R.drawable.bubble_frame5)
        resIdList.add(R.drawable.bubble_frame6)
        resIdList.add(R.drawable.bubble_frame7)
        resIdList.add(R.drawable.bubble_frame8)
        resIdList.add(R.drawable.bubble_frame9)
        resIdList.add(R.drawable.bubble_frame10)
        resIdList.add(R.drawable.bubble_frame11)
        resIdList.add(R.drawable.bubble_frame12)

        resIdList2.add(R.drawable.shark0001)
        resIdList2.add(R.drawable.shark0002)
        resIdList2.add(R.drawable.shark0003)
        resIdList2.add(R.drawable.shark0004)
        resIdList2.add(R.drawable.shark0005)



        resIdList.forEach {
            bitmapList.add(BitmapFactory.decodeResource(resources, it))
        }


        val background = getBackground(this, "bubble_frame1", 100, true)
        tv9Patch2?.background = background
        if (background is AnimationDrawable) {
            tv9Patch2?.post {
                background.start()
            }
        }
        tv9Patch3Bg()
        tv9Patch4Bg()


    }

    private fun tv9Patch3Bg() {
        val animationDrawable = AnimationDrawable()

        resIdList.forEach {

            //竖向拉伸像素43-51像素

            val drawable =
                NinePatchDrawableBuilder2().setResourceData(
                    resources,
                    it,
                    false
                )
                    .setPatchHorizontal(PatchRegionBean2(60, 61))
                    .setPatchVertical(PatchRegionBean2(52, 53))
                    //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
                    //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
                    .setPadding(35, 93, 37, 75)
                    .setOriginSize(128, 112)
                    .build()
            if (drawable != null) {
                animationDrawable.addFrame(drawable, 100)
            }
        }

        animationDrawable.isOneShot = false
        tv9Patch3?.background = animationDrawable
        animationDrawable.start()

    }

    private fun tv9Patch4Bg() {
        val animationDrawable = AnimationDrawable()

        val currentTimeMillis = System.currentTimeMillis()

        Log.i(TAG, "tv9Patch4Bg: start at $currentTimeMillis")
        resIdList2.forEach {

            //竖向拉伸像素43-51像素

            val drawable =
                NinePatchDrawableBuilder2().setResourceData(
                    resources,
                    it,
                    false
                )
                    .setPatchHorizontal(PatchRegionBean2(59, 60))
                    .setPatchVertical(PatchRegionBean2(62, 63))
                    //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
                    //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
                    .setPadding(56, 70, 43, 70)
                    .setOriginSize(128, 112)
                    .build()
            if (drawable != null) {
                animationDrawable.addFrame(drawable, 100)
            }
        }
        Log.i(
            TAG,
            "tv9Patch4Bg: end 生成帧动画，耗时：${System.currentTimeMillis() - currentTimeMillis} ms"
        )

        animationDrawable.isOneShot = false
        rl9Patch4?.background = animationDrawable
        animationDrawable.start()
    }


    private fun getBackground(
        context: Context,
        id: String,
        duration: Int,
        isSelf: Boolean
    ): Drawable? {
        val file = context.getExternalFilesDir(null)
            ?: return null

        val pngsDir: File = if (isSelf) {
            File(
                file.absolutePath + File.separator + "bubblepng" + File.separator + "bubbleframe"
            )
        } else {
            File(
                file.absolutePath + File.separator + "bubble4" + File.separator + "bubbleframe"
            )
        }
        if (!pngsDir.exists()) {
            return null
        }
        val files = pngsDir.listFiles()
        if (files == null || files.isEmpty()) {
            return null
        }
        val currentTimeMillis = System.currentTimeMillis()
        Log.i(
            TAG, "getBackground start at $currentTimeMillis"
        )
        val animationDrawable = AnimationDrawable()
        for (png in files) {
            val ninePatchDrawable: Drawable? =
                NinePatchDrawableBuilder2().setFileData(context.resources, png, false)
                    .setPatchHorizontal(PatchRegionBean2(60, 61))
                    .setPatchVertical(PatchRegionBean2(52, 53))
                    .setPadding(35, 93, 37, 75)
                    .setOriginSize(128, 112)
                    .build()
            if (ninePatchDrawable != null) {
                animationDrawable.addFrame(ninePatchDrawable, duration)
            }
        }
        animationDrawable.isOneShot = false
        Log.i(
            TAG,
            "getBackground end at " + System.currentTimeMillis() + " cost " + (System.currentTimeMillis() - currentTimeMillis)
        )
        return animationDrawable
    }


}