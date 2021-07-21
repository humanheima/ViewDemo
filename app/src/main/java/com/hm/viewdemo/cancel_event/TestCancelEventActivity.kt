package com.hm.viewdemo.cancel_event

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2020-03-02
 * Desc:
 *
 * 1. 父View收到ACTION_DOWN，如果没有拦截事件，则ACTION_DOWN事件被子视图接收，并且子视图消费了ACTION_DOWN事件，父视图后续事件会发送到子View。
 *
 * 2. 此时如果在父View中拦截ACTION_UP或ACTION_MOVE，子视图就接收不到ACTION_UP或ACTION_MOVE事件了，同时子视图会收到ACTION_CANCEL事件。
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
