package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.ninepatch.NinePatchDrawableBuilder
import com.hm.viewdemo.ninepatch.PatchRegionBean


/**
 * Created by p_dmweidu on 2023/10/22
 * Desc: NinePatchDrawable 测试点9图的使用
 */
class NinePatchDrawableActivity : AppCompatActivity() {


    private var tv9Patch1: TextView? = null
    private var tv9Patch2: TextView? = null
    private var tv9Patch3: TextView? = null
    private var tv9Patch4: TextView? = null

    private var resIdList: MutableList<Int> = mutableListOf()
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

        resIdList.forEach {
            bitmapList.add(BitmapFactory.decodeResource(resources, it))
        }


        tv9Patch3Bg()
        tv9Patch4Bg()


    }

    private fun tv9Patch3Bg() {
        val animationDrawable = AnimationDrawable()

//        resIdList.forEach {
//
//            //竖向拉伸像素43-51像素
//
//            val drawable =
//                NinePatchDrawableBuilder().setResourceData(
//                    resources,
//                    it,
//                    false
//                )
//                    .setPatchHorizontal(PatchRegionBean(0.43f, 0.47f))
//                    .setPatchVertical(PatchRegionBean(0.38f, 0.455f))
//                    //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
//                    //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
//                    .setPadding(0.234f, 0.258f, 0.42f, 0.40f)
//                    .build()
//            if (drawable != null) {
//                animationDrawable.addFrame(drawable, 100)
//            }
//        }
        val currentTimeMillis = System.currentTimeMillis()

        Log.i(TAG, "tv9Patch3Bg: start at $currentTimeMillis")

        bitmapList.forEach {


            //竖向拉伸像素43-51像素

            val drawable =
                NinePatchDrawableBuilder().setBitmapData(it, resources, false)
                    .setPatchHorizontal(PatchRegionBean(0.43f, 0.47f))
                    .setPatchVertical(PatchRegionBean(0.38f, 0.455f))
                    //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
                    //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
                    .setPadding(0.234f, 0.258f, 0.42f, 0.40f)
                    .build()
            if (drawable != null) {
                animationDrawable.addFrame(drawable, 100)
            }
        }

        Log.i(
            TAG,
            "tv9Patch3Bg: end 生成帧动画，耗时：${System.currentTimeMillis() - currentTimeMillis} ms"
        )

        animationDrawable.isOneShot = false
        tv9Patch3?.background = animationDrawable
        animationDrawable.start()


    }

    private fun tv9Patch4Bg() {
        val animationDrawable = AnimationDrawable()

        val currentTimeMillis = System.currentTimeMillis()

        Log.i(TAG, "tv9Patch4Bg: start at $currentTimeMillis")
        resIdList.forEach {

            //竖向拉伸像素43-51像素

            val drawable =
                NinePatchDrawableBuilder().setResourceData(
                    resources,
                    it,
                    true
                )
                    .setPatchHorizontal(PatchRegionBean(0.43f, 0.47f))
                    .setPatchVertical(PatchRegionBean(0.38f, 0.455f))
                    //注意计算paddingRight的使用，要用宽度减去底部黑线的end坐标，然后除以宽度
                    //注意计算paddingBottom的使用，要用高度减去右侧黑线的bottom坐标，然后除以高度
                    .setPadding(0.234f, 0.258f, 0.42f, 0.40f)
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
        tv9Patch4?.background = animationDrawable
        animationDrawable.start()


    }
}