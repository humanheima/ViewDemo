package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ImageUtil
import com.hm.viewdemo.widget.hongyang.RoundImageView

/**
 * Created by p_dmweidu on 2023/5/28
 * Desc: 聊天的多个头像，支持显示三个头像，或者一个头像。
 */
class ChatThreeAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val TAG = "OverlapAvatarView"

    private var mRadius = 0f
    private var borderColor = 0
    private var borderWidth = 0f
    private var avatarSize = 40f
    private var overlapSize = 10f

    private var ivOnlyOneAvatar: RoundImageView? = null

    private var iv1: RoundImageView? = null
    private var iv2: RoundImageView? = null
    private var iv3: RoundImageView? = null


    init {
//        val a = context.obtainStyledAttributes(attrs, R.styleable.OverlapAvatarView)
//        mRadius = a.getDimension(R.styleable.OverlapAvatarView_avatar_round_width, 0f)
//        borderColor = a.getColor(R.styleable.OverlapAvatarView_avatar_border_color, borderColor)
//        borderWidth = a.getDimension(R.styleable.OverlapAvatarView_avatar_border_width, borderWidth)
//        avatarSize = a.getDimension(R.styleable.OverlapAvatarView_avatar_size, avatarSize)
//        overlapSize = a.getDimension(R.styleable.OverlapAvatarView_avatar_overlap_size, overlapSize)
//        a.recycle()
        inflate(context, R.layout.chat_three_avatar_view, this)
        iv1 = findViewById(R.id.iv_avatar_1)
        iv2 = findViewById(R.id.iv_avatar_2)
        iv3 = findViewById(R.id.iv_avatar_3)

    }

    /**
     * 第1个 margin = 0
     * 第2个 margin = avatarSize - overlapSize
     * 第3个 margin = 2avatarSize - 2overlapSize
     * 第4个 margin = 3avatarSize - 3overlapSize
     */
    fun setAvatarList(avatarList: MutableList<String>?) {
        if (context == null) {
            return
        }
        if (avatarList == null || avatarList.size == 0) {
            return
        }
        val size = avatarList.size

        var finalList = mutableListOf<String>()

        if (size > 3) {
            finalList.addAll(avatarList.subList(0, 3))
        } else {
            finalList.addAll(avatarList)
        }

        val finalSize = finalList.size

        //removeAllViews()


        if (finalSize == 1) {
            ivOnlyOneAvatar?.visibility = View.VISIBLE
            iv1?.visibility = View.GONE
            iv2?.visibility = View.GONE
            iv3?.visibility = View.GONE
            ImageUtil.loadImage(context, avatarList[0], ivOnlyOneAvatar)

        } else {
            ivOnlyOneAvatar?.visibility = View.GONE
            iv1?.visibility = View.VISIBLE
            iv2?.visibility = View.VISIBLE
            iv3?.visibility = View.VISIBLE
            if (size == 2) {
                ImageUtil.loadImage(context, avatarList[0], iv1)
                ImageUtil.loadImage(context, avatarList[1], iv2)
                iv3?.setImageResource(R.drawable.avatar_square)
            } else if (size == 3) {
                ImageUtil.loadImage(context, avatarList[0], iv1)
                ImageUtil.loadImage(context, avatarList[1], iv2)
                ImageUtil.loadImage(context, avatarList[2], iv3)
            }
        }

    }

}