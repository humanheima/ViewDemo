package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.extension.dp2px


/**
 * Created by p_dmweidu on 2023/8/10
 * Desc: 测试Matrix的使用
 *
 * matrix.postTranslate((dx + 0.5f), (dy + 0.5f)), x> 0，把 bitmap向右移动
 */
class MatrixTestActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, MatrixTestActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix_test)


        val imageView = findViewById<ImageView>(R.id.iv_dog)
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.balloon)

        val matrix: Matrix = Matrix()
        val scale = 2.0f // 不缩放
        val dx = -100f // dx > 0
        val dy = 0f
        matrix.setScale(scale, scale)
        matrix.postTranslate((dx + 0.5f), (dy + 0.5f))

        //val transformedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.width, originalBitmap.height, matrix, true)
        val dp2px = 369.dp2px(this)
        val dp2px1 = 199.dp2px(this)
        val targetBitmap = Bitmap.createBitmap(
            dp2px,
            dp2px1,
            Bitmap.Config.ARGB_8888
        )
        applyMatrix(originalBitmap, targetBitmap, matrix)
        imageView.setImageBitmap(targetBitmap)
    }

    private fun applyMatrix(
        inBitmap: Bitmap, targetBitmap: Bitmap, matrix: Matrix
    ) {
        try {
            val canvas = Canvas(targetBitmap)
            canvas.drawColor(Color.GRAY)
            canvas.drawBitmap(inBitmap, matrix, null)
            canvas.setBitmap(null)
        } finally {
        }
    }
}
