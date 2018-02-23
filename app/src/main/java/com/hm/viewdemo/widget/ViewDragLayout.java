package com.hm.viewdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by dumingwei on 2017/6/27.
 */
public class ViewDragLayout extends LinearLayout {

    private static final String TAG = "ViewDragLayout";
    private ViewDragHelper viewDragHelper;


    public ViewDragLayout(Context context) {
        this(context, null);
    }

    public ViewDragLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
                    @Override
                    public boolean tryCaptureView(View child, int pointerId) {
                        return true;
                    }

                    @Override
                    public int clampViewPositionVertical(View child, int top, int dy) {
                        Log.e(TAG, "top=" + top + "dy=" + dy);
                        //top表示child最上边的移动范围
                        int topBound = getPaddingTop();
                        int bottomBound = getHeight() - getPaddingBottom() - child.getHeight();
                        top = Math.min(Math.max(top, topBound), bottomBound);
                        return top;
                    }

                    @Override
                    public int clampViewPositionHorizontal(View child, int left, int dx) {
                        Log.e(TAG, "left=" + left + "dx=" + dx);
                        //left 表示被拖动的子view的最左边可移动范围
                        int leftBound = getPaddingLeft();
                        int rightBound = getWidth() - getPaddingRight() - child.getWidth();
                        left = Math.min(Math.max(left, leftBound), rightBound);
                        return left;
                    }

                    @Override
                    public int getViewHorizontalDragRange(View child) {
                        return getMeasuredWidth()-child.getMeasuredWidth();
                    }

                    @Override
                    public int getViewVerticalDragRange(View child) {
                        return getMeasuredHeight()-child.getMeasuredHeight();
                    }
                }
        );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
}
