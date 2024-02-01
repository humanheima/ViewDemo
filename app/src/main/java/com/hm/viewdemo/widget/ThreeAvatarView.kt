package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ImageUtil
import com.hm.viewdemo.widget.hongyang.RoundImageView

/**
 * Created by p_dmweidu on 2023/5/28
 * Desc: 显示多个头像的View，根据传入的列表数量显示。
 */
class ThreeAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val TAG = "ThreeAvatarView"

    private var avatarSize: Int = 40
    private var overlapSize: Int = 10

    fun setSizeInDp(avatarSize: Int, overlapSize: Int) {
        this.avatarSize = avatarSize
        this.overlapSize = overlapSize
        if (this.overlapSize > avatarSize) {
            this.overlapSize = avatarSize
        }
        Log.i(TAG, "setSizeInDp: avatarSize = $avatarSize,overlapSize = $overlapSize")
    }

    /**
     * 第1个 margin = 0
     * 第2个 margin = avatarSize - overlapSize
     * 第3个 margin = 2avatarSize - 2overlapSize
     * 第4个 margin = 3avatarSize - 3overlapSize
     */
    fun setData(list: MutableList<String>?, reverse: Boolean = false) {
        if (context == null) {
            return
        }
        if (list == null || list.size == 0) {
            return
        }
        removeAllViews()
        if (reverse) {
            list.reverse()
            val endIndex = list.size - 1
            for (index in endIndex downTo 0) {
                val imageView = RoundImageView(context)
                val layoutParams = LayoutParams(avatarSize, avatarSize)
                var marginEnd = (avatarSize - overlapSize) * index
                if (marginEnd < 0) {
                    marginEnd = 0
                }
                layoutParams.marginEnd = marginEnd
                Log.i(TAG, "setData: marginEnd = $marginEnd")
                imageView.setImageResource(R.drawable.ic_soft_avatar)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                addView(imageView, layoutParams)
                ImageUtil.loadImage(context, list[index], imageView)

            }
        } else {
            list.forEachIndexed { index, s ->
                val imageView = RoundImageView(context)
                val layoutParams = LayoutParams(avatarSize, avatarSize)
                var marginStart = (avatarSize - overlapSize) * index
                if (marginStart < 0) {
                    marginStart = 0
                }
                layoutParams.marginStart = marginStart
                Log.i(TAG, "setData: marginStart = $marginStart")
                imageView.setImageResource(R.drawable.ic_soft_avatar)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                addView(imageView, layoutParams)
                ImageUtil.loadImage(context, s, imageView)
            }
        }

    }

}