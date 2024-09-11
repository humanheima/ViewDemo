package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.core.widget.NestedScrollView;

/**
 * Created by dumingwei on 2017/3/5.
 */
public class MyNestedScrollView2 extends NestedScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;

    private int mLastY = 0;

    // warning：2024/9/4: 这个值可能有点误差
    /**
     * 向上可以滚动的最大距离，当滚动距离 >= maxScrollY时，不再拦截并且手指从下向上滚动的时候，不再拦截事件。
     */
    int maxScrollY = 540;

    public void setMaxScrollY(int maxScrollY) {
        this.maxScrollY = maxScrollY;
    }

    private View innerView;

    private static final String TAG = "MyNestedScrollView2";


    public MyNestedScrollView2(Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MyNestedScrollView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int moveY = (int) ev.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onInterceptTouchEvent: getScrollY() = " + getScrollY());

                int deltaY = moveY - mLastY;
                Log.i(TAG, "onInterceptTouchEvent: deltaY = " + deltaY);

                /**
                 * 1. 当滑动距离 >= maxScrollY时 && 并且手指从下向上滚动的时候，不再拦截事件。
                 */
//                if (getScrollY() >= maxScrollY) {
//                    if (deltaY < 0) {
//                        return false;
//                    } else if (deltaY > 0) {
//                        return false;
//                    }
//
//                }

                /**
                 * 手指从下向上滚动的时候，如果滑动距离 >= maxScrollY时，不再拦截事件，让内部的控件处理
                 */
                if (deltaY < 0) {
                    if (getScrollY() >= maxScrollY) {
                        return false;
                    }
                } else if (deltaY > 0) {
                    /**
                     * 手指从上向下滚动，这个时候，如果内部的TextView getTop() >= 0 时，不再拦截，先让内部的TextView 回到顶部
                     */
                    if (interceptCallback != null && interceptCallback.dontIntercept()) {
                        return false;
                    }

                }

                break;
            default:
                break;
        }
        mLastY = moveY;
        return super.onInterceptTouchEvent(ev);
    }

    public interface InterceptCallback {
        boolean dontIntercept();
    }

    private InterceptCallback interceptCallback;

    public void setInterceptCallback(InterceptCallback interceptCallback) {
        this.interceptCallback = interceptCallback;
    }

    public void setInnerView(View innerView) {
        this.innerView = innerView;
    }
}
