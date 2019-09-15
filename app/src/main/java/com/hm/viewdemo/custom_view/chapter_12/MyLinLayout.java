package com.hm.viewdemo.custom_view.chapter_12;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by qijian on 15/7/22.
 */
public class MyLinLayout extends ViewGroup {

    private static final String TAG = "MyLinLayout";

    public MyLinLayout(Context context) {
        super(context);
    }

    public MyLinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int height = 0;
        int width = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            height += childHeight;
            width = Math.max(childWidth, width);
        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY) ? measureWidth
                : width, (measureHeightMode == MeasureSpec.EXACTLY) ? measureHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {

            View child = getChildAt(i);

            MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            int topMargin = lp.topMargin;
            int bottomMargin = lp.bottomMargin;

            //在布局之前先加上topMargin
            top += topMargin;

            child.layout(0, top, childWidth, top + childHeight);

            //布局之后加上childHeight和bottomMargin
            top += childHeight;
            top += bottomMargin;
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
    }

}
