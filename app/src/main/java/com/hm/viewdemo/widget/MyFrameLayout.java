package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by dumingwei on 2019/2/28.
 * Desc:
 */
public class MyFrameLayout extends FrameLayout {


    private static final String TAG = "MyFrameLayout";
    private boolean shouldIntercept = false;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Log.e(TAG, "MyFrameLayout: ");
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "dispatchTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "dispatchTouchEvent ACTION_MOVE");
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent ACTION_UP");
                action = "ACTION_UP";
                break;
        }

        boolean handled = super.dispatchTouchEvent(event);
        Log.e(TAG, "dispatchTouchEvent: " + action + " handled = " + handled);
        return handled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onInterceptTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                return false;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent ACTION_MOVE");
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onInterceptTouchEvent ACTION_UP");
                action = "ACTION_UP";
                break;
        }

        boolean handled = super.onInterceptTouchEvent(event);
        Log.e(TAG, "onInterceptTouchEvent: " + action + " handled = " + handled);
        return handled;
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public boolean isShouldIntercept() {
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_DOWN");
                action = "ACTION_DOWN";
                return true;
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
        Log.e(TAG, "onTouchEvent: " + action + " handled = " + handled);
        return handled;
    }
}
