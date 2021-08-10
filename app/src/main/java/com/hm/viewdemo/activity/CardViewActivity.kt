package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.RoundRectDrawableWithShadow
import kotlin.math.ceil

/**
 * Crete by dumingwei on 2019-08-01
 * Desc:
 *
 * 参考链接：https://www.jianshu.com/p/b105019028b6
 */
class CardViewActivity : AppCompatActivity() {


    private val TAG: String = "CardViewActivity"

    private val SHADOW_MULTIPLIER = 1.5f

    private val COS_45 = Math.cos(Math.toRadians(45.0))


    private lateinit var flManualSetBg: FrameLayout

    //圆角12dp
    private var mRadius: Int = 12
    //阴影和最大阴影都设为16dp
    private var mElevation: Int = 16
    private var mMaxElevation: Int = 16

    //5.0版本以下，CardView设置的竖直方向上和水平方向上的padding
    private var verticalPadding: Int = 0
    private var horizontalPadding: Int = 0


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_view)
        Log.i(TAG, "onCreate: COS_45 = $COS_45")

        flManualSetBg = findViewById(R.id.flManualSetBg)

        mRadius = ScreenUtil.dpToPx(this, 12)
        mElevation = ScreenUtil.dpToPx(this, 16)
        mMaxElevation = ScreenUtil.dpToPx(this, 16)

        verticalPadding = ceil((SHADOW_MULTIPLIER * mMaxElevation + (1 - COS_45) * mRadius)).toInt()
        horizontalPadding = ceil((mMaxElevation + (1 - COS_45) * mRadius)).toInt()

        RoundRectDrawableWithShadow.sRoundRectHelper = RoundRectDrawableWithShadow.RoundRectHelper { canvas, bounds, cornerRadius, paint -> canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, paint) }

        var backgroundColor: ColorStateList = ColorStateList.valueOf(resources.getColor(androidx.cardview.R.color.cardview_light_background))

        val drawable = RoundRectDrawableWithShadow(resources, backgroundColor, mRadius.toFloat(), mElevation.toFloat(), mMaxElevation.toFloat())

        flManualSetBg.setBackgroundDrawable(drawable)

        flManualSetBg.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)


    }


}
