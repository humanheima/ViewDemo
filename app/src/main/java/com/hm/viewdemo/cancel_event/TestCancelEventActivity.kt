package com.hm.viewdemo.cancel_event

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2020-03-02
 * Desc: ACTION_CANCEL 事件
 * 简单的说，就是 View，收到了ACTION_DOWN事件，但是后续的 ACTION_UP 或 ACTION_MOVE 事件没有收到，就会收到一个 ACTION_CANCEL 事件。
 *
 * 1.父View收到 ACTION_DOWN ，如果没有拦截事件，则 ACTION_DOWN 事件被子视图接收，并且子视图消费了 ACTION_DOWN 事件，父视图后续事件会发送到子View。
 *
 * 2. 此时如果在父View中拦截 ACTION_UP 或 ACTION_MOVE ，子视图就接收不到 ACTION_UP 或 ACTION_MOVE 事件了，同时子视图会收到 ACTION_CANCEL 事件。
 * 一般ACTION_CANCEL和ACTION_UP都作为View一段事件处理的结束。
 * 参考链接：https://blog.csdn.net/starry_eve/article/details/46439437
 *
 */
class TestCancelEventActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, TestCancelEventActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_cancel_event)
    }
}
