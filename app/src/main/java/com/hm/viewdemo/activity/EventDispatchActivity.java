package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.EventDispatchButton;

import java.lang.ref.WeakReference;

public class EventDispatchActivity extends BaseActivity {

    private static final String TAG = "EventDispatchActivity";

    private EventDispatchButton btnTestEvent;
    private MyHandler handler;

    public static void launch(Context context) {
        Intent starter = new Intent(context, EventDispatchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_event_dispatch;
    }

    @Override
    protected void initData() {
        handler = new MyHandler(this);
        btnTestEvent = (EventDispatchButton) findViewById(R.id.btn_touch_event);
        btnTestEvent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        /**
                         * 当前view 在down事件中返回了false,view依然可以收到move事件
                         * 因为onTouch方法返回false以后，View的onTouchEvent会被调用，这里会进行判断
                         * 如果 View是 CLICKABLE 或者 LONG_CLICKABLE 那么 CONTEXT_CLICKABLE 的话，
                         * onTouchEvent返回true
                         */
                        Log.d(TAG, "onTouch MotionEvent.ACTION_DOWN 方法返回false");
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "onTouch MotionEvent.ACTION_MOVE 方法返回false");
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "onTouch MotionEvent.ACTION_UP 方法返回false");
                        return true;
                    default:
                        return true;
                }
                /**
                 *  如果给一个View设置了OnTouchListener，如果返回onTouch 返回true，View的
                 *  onTouchEvent方法就不会被执行，那么即使给View添加了OnClickListener事件，
                 *  onClick方法也不会执行
                 */
            }
        });
        btnTestEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventDispatchActivity.this, "onClick 方法", Toast.LENGTH_SHORT).show();
            }
        });

        Message message = handler.obtainMessage();
        handler.sendMessage(message);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "Activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "Activity onTouchEvent");
        return super.onTouchEvent(event);
    }

    public void testHandler() {
        Log.e(TAG, "testHandler");
    }


    private static class MyHandler extends Handler {

        private WeakReference<EventDispatchActivity> weakReference;

        public MyHandler(EventDispatchActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EventDispatchActivity activity = weakReference.get();
            activity.testHandler();
        }
    }
}
