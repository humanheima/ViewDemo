package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ImageUtil
import com.hm.viewdemo.widget.hongyang.RoundImageView

/**
 * Created by p_dmweidu on 2023/5/28
 * Desc: 显示多个头像的View，根据传入的列表数量显示。
 */
class OverlapAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val TAG = "OverlapAvatarView"

    private var mRadius = 0f
    private var borderColor = 0
    private var borderWidth = 0f
    private var avatarSize = 40f
    private var overlapSize = 10f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.OverlapAvatarView)
        mRadius = a.getDimension(R.styleable.OverlapAvatarView_avatar_round_width, 0f)
        borderColor = a.getColor(R.styleable.OverlapAvatarView_avatar_border_color, borderColor)
        borderWidth = a.getDimension(R.styleable.OverlapAvatarView_avatar_border_width, borderWidth)
        avatarSize = a.getDimension(R.styleable.OverlapAvatarView_avatar_size, avatarSize)
        overlapSize = a.getDimension(R.styleable.OverlapAvatarView_avatar_overlap_size, overlapSize)
        a.recycle()
    }

    /**
     * 第1个 margin = 0
     * 第2个 margin = avatarSize - overlapSize
     * 第3个 margin = 2avatarSize - 2overlapSize
     * 第4个 margin = 3avatarSize - 3overlapSize
     */
    fun setAvatarList(avatarList: MutableList<String?>?) {
        if (context == null) {
            return
        }
        if (avatarList == null || avatarList.size == 0) {
            return
        }
        removeAllViews()
        for (index in avatarList.size - 1 downTo 0) {
            val imageView = RoundImageView(context)
            val layoutParams = RelativeLayout.LayoutParams(avatarSize.toInt(), avatarSize.toInt())
            var marginStart = (avatarSize - overlapSize) * index
            if (marginStart < 0) {
                marginStart = 0f
            }
            layoutParams.marginStart = marginStart.toInt()
            addView(imageView, layoutParams)
            ImageUtil.loadImage(context, avatarList[index], imageView)
        }
    }

}