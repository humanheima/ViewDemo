package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by dumingwei on 2017/2/28.
 */
public class ListViewEx extends ListView {

    private final String TAG = getClass().getSimpleName();
    private HorizontalScrollViewEx2 mHorizontalScrollViewEx2;

    private int mLastX = 0;
    private int mLastY = 0;

    public void setmHorizontalScrollViewEx2(HorizontalScrollViewEx2 mHorizontalScrollViewEx2) {
        this.mHorizontalScrollViewEx2 = mHorizontalScrollViewEx2;
    }

    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.i(TAG, "deltaX:" + deltaX + " deltaY:" + deltaY);
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }
}

