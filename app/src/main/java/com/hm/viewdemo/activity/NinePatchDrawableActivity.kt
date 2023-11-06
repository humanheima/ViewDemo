package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.ninepatch.NinePatchDrawableBuilder4
import com.hm.viewdemo.ninepatch.PatchStretchBean
import java.io.File


/**
 * Created by p_dmweidu on 2023/10/22
 * Desc: NinePatchDrawable 测试点9图的使用
 */
class NinePatchDrawableActivity : AppCompatActivity() {

    private var tv9Patch2: TextView? = null
    private var tv9Patch3: TextView? = null
    private var tv9Patch4: TextView? = null
    private var tv9Patch5: TextView? = null

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

        tv9Patch2 = findViewById(R.id.tv_9_patch2)
        tv9Patch3 = findViewById(R.id.tv_9_patch3)
        tv9Patch4 = findViewById(R.id.tv_9_patch4)

        rl9Patch4 = findViewById(R.id.rl_9_patch4)

        tv9Patch5 = findViewById(R.id.tv_9_patch5)

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

        getDrawableFromResource()
        getDrawableFromResource2()

        getDrawableFromFile()
        getDrawableFromFile2()

    }

    private fun getDrawableFromResource() {
        NinePatchDrawableBuilder4().getAnimationDrawableFromResource(
            resources,
            resIdList,
            PatchStretchBean(60, 61),
            PatchStretchBean(52, 53),
            Rect(31, 37, 90, 75),
            128,
            112
        )?.let {
            it.isOneShot = false
            tv9Patch3?.background = it
            it.start()
        }

    }

    private fun getDrawableFromResource2() {
        NinePatchDrawableBuilder4().getAnimationDrawableFromResource(
            resources,
            resIdList2,
            PatchStretchBean(59, 60),
            PatchStretchBean(62, 63),
            Rect(56, 43, 70, 70),
            128,
            112
        )?.let {
            it.isOneShot = false
            rl9Patch4?.background = it
            it.start()
        }
    }


    private fun getDrawableFromFile() {
        val background = getBackground(this, "bubblepng/bubbleframe")
        tv9Patch2?.background = background
        if (background is AnimationDrawable) {
            tv9Patch2?.post {
                background.start()
            }
        }
    }

    private fun getBackground(
        context: Context, pngDirName: String
    ): Drawable? {
        val dir = context.getExternalFilesDir(null)
            ?: return null
        val pngsDir: File = File(dir, pngDirName)
        if (!pngsDir.exists()) {
            return null
        }
        val files = pngsDir.listFiles()
        if (files == null || files.isEmpty()) {
            return null
        }

        val animationDrawable2 = NinePatchDrawableBuilder4().getAnimationDrawableFromFile(
            context,
            context.resources,
            true,
            pngsDir,
            PatchStretchBean(60, 61),
            PatchStretchBean(52, 53),
            Rect(31, 37, 90, 75),
            128,
            112
        )
        animationDrawable2?.isOneShot = false

        return animationDrawable2
    }

    private fun getDrawableFromFile2() {
        val background = getBackground2(this, "bubble_two")
        tv9Patch5?.background = background
        if (background is AnimationDrawable) {
            tv9Patch5?.post {
                background.start()
            }
        }
    }

    private fun getBackground2(
        context: Context, pngDirName: String
    ): Drawable? {
        val dir = context.getExternalFilesDir(null)
            ?: return null
        val pngsDir: File = File(dir, pngDirName)
        if (!pngsDir.exists()) {
            return null
        }
        val files = pngsDir.listFiles()
        if (files == null || files.isEmpty()) {
            return null
        }

        val animationDrawable2 = NinePatchDrawableBuilder4().getAnimationDrawableFromFile(
            context,
            context.resources,
            false,
            pngsDir,
            PatchStretchBean(113, 115),
            PatchStretchBean(110, 112),
            Rect(87, 84, 141, 138),
            228,
            222
        )
        animationDrawable2?.isOneShot = false

        return animationDrawable2
    }


}