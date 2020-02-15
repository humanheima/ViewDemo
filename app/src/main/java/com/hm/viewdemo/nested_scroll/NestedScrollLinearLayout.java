package com.hm.viewdemo.nested_scroll;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hm.viewdemo.R;

/**
 * Crete by dumingwei on 2020-02-15
 * Desc:
 */
public class NestedScrollLinearLayout extends LinearLayout implements NestedScrollingParent {


    private static final String TAG = "NestedScrollLinearLayou";

    private int topViewHieght = 0;

    private RelativeLayout rlTopView;
    private RecyclerView rv;

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    public NestedScrollLinearLayout(Context context) {
        super(context);
    }

    public NestedScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rlTopView = findViewById(R.id.rlTopView);
        rv = findViewById(R.id.rv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: " + getMeasuredHeight() + ","
                + rlTopView.getMeasuredHeight() + "," + rv.getMeasuredHeight());
        ViewGroup.LayoutParams params = rv.getLayoutParams();
        params.height = rv.getMeasuredHeight() + rlTopView.getMeasuredHeight();

        setMeasuredDimension(
                getMeasuredWidth(), rlTopView.getMeasuredHeight() + rv.getMeasuredHeight()
        );

        Log.d(TAG, "onMeasure: " + getMeasuredHeight() + ","
                + rlTopView.getMeasuredHeight() + "," + rv.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        topViewHieght = rlTopView.getMeasuredHeight();
        Log.d(TAG, "onSizeChanged: topViewHieght=" + topViewHieght);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // 向上滑动（手指从下向上滑）, dy>0
        // 向下滑动（手指从上向下滑）, dy<0
        Log.d(TAG, "onNestedPreScroll: getY= " + getY());
        Log.d(TAG, "onNestedPreScroll: getTop= " + getTop());
        Log.d(TAG, "onNestedPreScroll: getTranslationY=" + getTranslationY());
        /**
         * scrollY>=0，向上滑动的时候，scrollY增加。
         */
        // 向上滑动（手指从下向上滑）, dy>0
        boolean hiddenTop = dy > 0 && getScrollY() < topViewHieght;

        // dy<0 向下滑动（手指从上向下滑）
        boolean showTop = dy < 0 && getScrollY() >= 0;
        if (hiddenTop || showTop) {
            //滑动y距离
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        getNestedScrollingParentHelper().onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        getNestedScrollingParentHelper().onStopNestedScroll(child);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > topViewHieght) {
            y = topViewHieght;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    private NestedScrollingParentHelper getNestedScrollingParentHelper() {
        if (mNestedScrollingParentHelper == null) {
            mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        }
        return mNestedScrollingParentHelper;
    }
}