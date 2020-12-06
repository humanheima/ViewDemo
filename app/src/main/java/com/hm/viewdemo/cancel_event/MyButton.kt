package com.hm.viewdemo.cancel_event

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Button

/**
 * Crete by dumingwei on 2020-03-02
 * Desc: 用来测试ACTION_CANCEL事件的触发条件
 *
 * step1. 父View收到ACTION_DOWN，如果没有拦截事件，则ACTION_DOWN前驱事件被子视图接收，父视图后续事件会发送到子View。

step2. 此时如果在父View中拦截ACTION_UP或ACTION_MOVE，在第一次父视图拦截消息的瞬间，父视图指定子视图不接受后续消息了，同时子视图会收到ACTION_CANCEL事件。



一般ACTION_CANCEL和ACTION_UP都作为View一段事件处理的结束。
————————————————
版权声明：本文为CSDN博主「LiteHeaven」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/starry_eve/article/details/46439437
 *
 */
class MyButton : Button {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "dispatchTouchEvent: " + event.action)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.i(TAG, "onTouchEvent: ${event.action}")
        return super.onTouchEvent(event)
    }

    companion object {

        private val TAG = "MyButton"
    }

}
