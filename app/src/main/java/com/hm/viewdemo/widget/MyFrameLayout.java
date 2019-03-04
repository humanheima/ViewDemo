package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by dumingwei on 2019/2/28.
 * Desc:
 */
public class MyFrameLayout extends FrameLayout {


    private boolean shouldIntercept = false;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (shouldIntercept) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setShouldIntercept(boolean shouldIntercept) {
        this.shouldIntercept = shouldIntercept;
    }

    public boolean isShouldIntercept() {
        return shouldIntercept;
    }
}
