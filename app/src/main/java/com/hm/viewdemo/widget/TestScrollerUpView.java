package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import com.hm.viewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2020/12/28
 * <p>
 * Desc: 测试向上滚动的View
 */
public class TestScrollerUpView extends View {

    private final String TAG = getClass().getSimpleName();

    private Scroller mScroller;
    /**
     * 绘制前面的文字的画笔
     */
    private Paint mPaint;

    /**
     * 绘制"元"字的画笔
     */
    private Paint mRmbPaint;

    private List<String> mData;

    /**
     * 第一个Item的绘制Text的坐标
     */
    private int mFirstItemDrawX, mFirstItemDrawY;

    private int bgColor;

    private int mItemHeight;
    private int scrollDistance;
    private int textColor;

    private String rmb = "元";
    private int animatorDuration;

    public TestScrollerUpView(Context context) {
        this(context, null);
    }

    public TestScrollerUpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScrollerUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTextColor(@ColorRes int textColor) {
        this.textColor = textColor;
    }

    private void init() {
        mData = new ArrayList<>();
        mData.add("昔闻洞庭水");
        mData.add("今上岳阳楼");
        mData.add("念去去");
        mData.add("千里烟波");
        mData.add("暮霭沉沉楚天阔");
        mData.add("多情自古伤离别");
        mData.add("更哪堪冷落清秋节");

        animatorDuration = 3000;

        bgColor = Color.LTGRAY;
        textColor = getResources().getColor(R.color.colorAccent);


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(textColor);
        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_20dp));

        mRmbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRmbPaint.setColor(textColor);
        mRmbPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size_14dp));

        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemHeight = getHeight();
        int allHeight = mItemHeight * mData.size();
        scrollDistance = allHeight - mItemHeight;
        mFirstItemDrawX = getWidth() / 2;
        //计算绘制第一个item时，画笔的Y坐标
        mFirstItemDrawY = (int) ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: mScroller.getFinalY()=" + mScroller.getFinalY());

        //绘制背景
        mPaint.setColor(bgColor);

        int width = getWidth();

        canvas.drawRect(0, 0, width, getHeight(), mPaint);

        mPaint.setColor(textColor);

        //设置居中对齐
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mData.size(); i++) {
            String data = mData.get(i);
            float length = mPaint.measureText(data);
            Log.d(TAG, "onDraw:文字长度 " + length);
            int itemDrawY = mFirstItemDrawY + i * mItemHeight;
            canvas.drawText(data, mFirstItemDrawX, itemDrawY, mPaint);
            //绘制最后一个"元"字
            canvas.drawText(rmb, (width + length) / 2, itemDrawY, mRmbPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mItemHeight = getMeasuredHeight();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            scrollTo(0, currY);
            invalidate();
        }
    }

    /**
     * 从0滚动到scrollDistance
     */
    public void smoothScrollUp() {
        int scrollY = getScrollY();
        if (scrollY != 0) {
            return;
        }
        mScroller.startScroll(0, 0, 0, scrollDistance, animatorDuration);
        invalidate();
    }

    /**
     * 从scrollDistance滚动到0
     */
    public void smoothScrollDown() {
        int scrollY = getScrollY();
        if (scrollY != scrollDistance) {
            return;
        }
        mScroller.startScroll(0, scrollY, 0, -scrollDistance, animatorDuration);
        invalidate();
    }

}
