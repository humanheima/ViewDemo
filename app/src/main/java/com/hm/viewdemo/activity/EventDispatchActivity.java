package com.hm.viewdemo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.EventDispatchButton;
import com.hm.viewdemo.widget.MyImageView;

public class EventDispatchActivity extends BaseActivity {

    private static final String TAG = "EventDispatchActivity";

    private EventDispatchButton btnTestEvent;
    private Button btnTestClickevent;
    private MyImageView ivTestClick;

    public static void launch(Context context) {
        Intent starter = new Intent(context, EventDispatchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_event_dispatch;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        btnTestEvent = findViewById(R.id.btn_touch_event);
//        btnTestEvent.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        /**
//                         * 当前view 在down事件中返回了false,view依然可以收到move事件
//                         * 因为onTouch方法返回false以后，View的onTouchEvent会被调用，这里会进行判断
//                         * 如果 View是 CLICKABLE 或者 LONG_CLICKABLE 那么 CONTEXT_CLICKABLE 的话，
//                         * onTouchEvent返回true
//                         */
//                        Log.i(TAG, "onTouch MotionEvent.ACTION_DOWN 方法返回false");
//                        return false;
//                    case MotionEvent.ACTION_MOVE:
//                        Log.i(TAG, "onTouch MotionEvent.ACTION_MOVE 方法返回false");
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        Log.i(TAG, "onTouch MotionEvent.ACTION_UP 方法返回false");
//                        return true;
//                    default:
//                        return true;
//                }
//                /**
//                 *  如果给一个View设置了OnTouchListener，如果返回onTouch 返回true，View的
//                 *  onTouchEvent方法就不会被执行，那么即使给View添加了OnClickListener事件，
//                 *  onClick方法也不会执行
//                 */
//            }
//        });
//        btnTestEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(EventDispatchActivity.this, "onClick 方法", Toast.LENGTH_SHORT).show();
//            }
//        });

        ivTestClick = findViewById(R.id.iv_test_click);
        ivTestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventDispatchActivity.this,
                        "ivTestClick 被点击",
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ivTestClick：" + ivTestClick);
            }
        });
        btnTestClickevent = findViewById(R.id.btn_test_image_view_click_event);
        btnTestClickevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: " + ivTestClick.isClickable());
                Toast.makeText(EventDispatchActivity.this,
                        "ivTestClick 可以点击吗？" + ivTestClick.isClickable(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.e(TAG, "dispatchTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "dispatchTouchEvent ACTION_MOVE");
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
               // Log.e(TAG, "dispatchTouchEvent ACTION_UP");
                action = "ACTION_UP";
                break;
        }

        boolean handled = super.dispatchTouchEvent(event);
        //Log.e(TAG, "dispatchTouchEvent: action = " + action + " handled = " + handled);
        return handled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.e(TAG, "onTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onTouchEvent ACTION_MOVE");
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
               //Log.e(TAG, "onTouchEvent ACTION_UP");
                action = "ACTION_UP";
                break;
        }
        boolean handled = super.onTouchEvent(event);
        //Log.e(TAG, "onTouchEvent: action = " + action + " handled = " + handled);
        return handled;
    }


}
