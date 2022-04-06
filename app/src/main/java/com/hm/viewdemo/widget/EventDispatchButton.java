package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dumingwei on 2017/10/7.
 */
public class EventDispatchButton extends View {

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
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent ACTION_MOVE");
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent ACTION_UP");
                action = "ACTION_UP";
                break;
        }

        boolean handled = super.onTouchEvent(event);
        Log.i(TAG, "onTouchEvent: action = " + action + " handled = " + handled);
        return handled;
    }

}
