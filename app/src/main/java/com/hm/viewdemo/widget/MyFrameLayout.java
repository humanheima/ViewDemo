package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputDevice;
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
        Log.e(TAG, "MyFrameLayout: ");
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*if (shouldIntercept) {
            return true;
        }*/
        if (ev.isFromSource(InputDevice.SOURCE_MOUSE)
                && ev.getAction() == MotionEvent.ACTION_DOWN
                && ev.isButtonPressed(MotionEvent.BUTTON_PRIMARY)) {
            Log.e(TAG, "onInterceptTouchEvent: return true");
            return true;
        }
        Log.e(TAG, "onInterceptTouchEvent: return false");
        return false;
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public boolean isShouldIntercept() {
        return shouldIntercept;
    }
}
