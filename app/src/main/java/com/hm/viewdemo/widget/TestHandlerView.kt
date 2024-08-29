package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by p_dmweidu on 2024/8/29
 * Desc: 如果View被detach了，这个时候，post的消息会被缓存到View的 HandlerActionQueue 中。不会被真正的handler
 * 执行。
 * 只有重新 attach 之后，才会真正执行前面post 的内容
 */
class TestHandlerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextView(context, attrs) {


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun post(action: Runnable?): Boolean {
        return super.post(action)
    }

}