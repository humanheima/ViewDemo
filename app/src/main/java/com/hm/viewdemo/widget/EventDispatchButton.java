package com.hm.viewdemo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dumingwei on 2017/10/7.
 */
public class EventDispatchButton extends AppCompatButton {

    private static final String TAG = "EventDispatchButton";

    public EventDispatchButton(Context context) {
        super(context);
    }

    public EventDispatchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDispatchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent ACTION_DOWN");
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent ACTION_UP");
                break;
        }*/
        return super.onTouchEvent(event);
    }
}
