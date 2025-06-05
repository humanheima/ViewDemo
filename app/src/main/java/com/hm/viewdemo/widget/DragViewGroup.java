package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

public class DragViewGroup extends FrameLayout {
    private ViewDragHelper mDragHelper;
    private View mDragView;
    private int mDragRangeLeft;
    private int mDragRangeRight;
    private int mDragRangeTop;
    private int mDragRangeBottom;

    public DragViewGroup(Context context) {
        this(context, null);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                // 限制水平拖动范围
                return Math.max(mDragRangeLeft, Math.min(left, mDragRangeRight));
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                // 限制垂直拖动范围
                return Math.max(mDragRangeTop, Math.min(top, mDragRangeBottom));
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                // 松手后处理吸附逻辑
                int finalLeft;
                int centerX = getWidth() / 2;
                if (releasedChild.getLeft() + releasedChild.getWidth() / 2 < centerX) {
                    // 靠左吸附
                    finalLeft = mDragRangeLeft;
                } else {
                    // 靠右吸附
                    finalLeft = mDragRangeRight;
                }
                mDragHelper.settleCapturedViewAt(finalLeft, releasedChild.getTop());
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return mDragRangeRight - mDragRangeLeft;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return mDragRangeBottom - mDragRangeTop;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 假设第一个子View是可拖动的View
        if (getChildCount() > 0) {
            mDragView = getChildAt(0);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置拖动范围，留出边界
        mDragRangeLeft = getPaddingLeft();
        mDragRangeTop = getPaddingTop();
        mDragRangeRight = w - (mDragView != null ? mDragView.getWidth() : 0) - getPaddingRight();
        mDragRangeBottom = h - (mDragView != null ? mDragView.getHeight() : 0) - getPaddingBottom();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}