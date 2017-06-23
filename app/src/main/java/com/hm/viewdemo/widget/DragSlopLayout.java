package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by dumingwei on 2017/6/23.
 * 仿网易新闻底部文字介绍布局
 */
public class DragSlopLayout extends FrameLayout {

    private static final String TAG = "DragSlopLayout";

    private int DEFAULT_FIX_HEIGHT;
    private int DEFAULT_MAX_HEIGHT;

    //向上拉的时候最高的高度
    private int maxHeight;
    //底部固定高度
    private int fixHeight;
    //拖动
    private ViewDragHelper viewDragHelper;
    private ViewDragHelper.Callback callback;

    public DragSlopLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragSlopLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSlopLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs, defStyleAttr);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragSlopLayout, defStyleAttr, 0);
        //默认140dp
        DEFAULT_FIX_HEIGHT = ScreenUtil.dpToPx(context, 140);
        DEFAULT_MAX_HEIGHT = ScreenUtil.dpToPx(context, 200);
        fixHeight = ta.getDimensionPixelOffset(R.styleable.DragSlopLayout_fix_height, DEFAULT_FIX_HEIGHT);
        maxHeight = ta.getDimensionPixelOffset(R.styleable.DragSlopLayout_max_height, DEFAULT_MAX_HEIGHT);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 1) {
            throw new IllegalArgumentException("DragLayout must contains only one childView");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (fixHeight < DEFAULT_FIX_HEIGHT) {
            fixHeight = DEFAULT_FIX_HEIGHT;
        }
        if (maxHeight < DEFAULT_MAX_HEIGHT) {
            maxHeight = DEFAULT_MAX_HEIGHT;
        }
        View childView = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        // 限定视图的最大高度
        if (childHeight > maxHeight) {
            childView.measure(MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(maxHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY));
        }
        //限定视图最小高度，必须大于等于fix_height
        if (childHeight < fixHeight) {
            childView.measure(MeasureSpec.makeMeasureSpec(childWidth - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(fixHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //如果测量高度measuredHeight大于fixHeight,则应该设置translationX属性，向下偏移
        View childView = getChildAt(0);
        MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        int childWidth = childView.getMeasuredWidth();
        int childHeight = childView.getMeasuredHeight();
        int childTop = bottom - fixHeight;
        //childView放置在父布局的底部
        childView.layout(lp.leftMargin, childTop, lp.leftMargin + childWidth, childTop + childHeight);
    }
}
