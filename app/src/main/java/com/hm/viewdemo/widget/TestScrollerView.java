package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.hm.viewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2018/4/15 0015.
 */
public class TestScrollerView extends View {

    private final String TAG = getClass().getSimpleName();

    private Scroller mScroller;
    private Paint mPaint;
    private int mItemHeight;
    private List<String> mData;
    private int mScrollOffsetY;
    /**
     * 最后手指Down事件的Y轴坐标，用于计算拖动距离
     */
    private int mLastDownY;
    private int mTouchDownY;
    private int mTouchSlop;
    /**
     * 该标记的作用是，令mTouchSlop仅在一个滑动过程中生效一次。
     */
    private boolean mTouchSlopFlag;
    /**
     * 整个控件的可绘制面积
     */
    private Rect mDrawnRect;
    /**
     * 中心被选中的Item的坐标矩形
     */
    private Rect mSelectedItemRect;
    /**
     * 第一个Item的绘制Text的坐标
     */
    private int mFirstItemDrawX, mFirstItemDrawY;
    /**
     * 中心的Item绘制text的Y轴坐标
     */
    private int mCenterItemDrawnY;
    /**
     * 显示的Item一半的数量（中心Item上下两边分别的数量）
     * 总显示的数量为 mHalfVisibleItemCount * 2 + 1
     */
    private int mHalfVisibleItemCount = 2;

    /**
     * 当前的Item的位置
     */
    private int mCurrentPosition = 0;

    /**
     * 最大可以Fling的距离
     */
    private int mMaxFlingY, mMinFlingY;

    private int mCurtainColor;
    private int mCurtainBorderColor;

    public TestScrollerView(Context context) {
        this(context, null);
    }

    public TestScrollerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScrollerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mData = new ArrayList<>();
        for (int i = 2018; i < 2038; i++) {
            mData.add(String.valueOf(i));
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_size));
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setStyle(Paint.Style.FILL);

        mDrawnRect = new Rect();
        mSelectedItemRect = new Rect();
        mScroller = new Scroller(getContext());

        mCurtainColor = Color.parseColor("#303d3d3d");
        mCurtainBorderColor = Color.BLACK;

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownY = mLastDownY = (int) event.getY();
                mTouchSlopFlag = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchSlopFlag && Math.abs(mTouchDownY - event.getY()) < mTouchSlop) {
                    break;
                }
                mTouchSlopFlag = false;
                float move = event.getY() - mLastDownY;
                mScrollOffsetY += move;
                mLastDownY = (int) event.getY();
                Log.d(TAG, "onTouchEvent: mScrollOffsetY=" + mScrollOffsetY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mTouchDownY == mLastDownY) {
                    performClick();
                } else {
                    mScroller.startScroll(0, mScrollOffsetY, 0,
                            computeDistanceToEndPoint(mScrollOffsetY % mItemHeight));
                }
                if (mScroller.getFinalY() > mMaxFlingY) {
                    mScroller.setFinalY(mMaxFlingY);
                    Log.d(TAG, "onTouchEvent: mScroller.getFinalY() > mMaxFlingY:" + mScroller.getFinalY() + "," + mMaxFlingY);
                } else if (mScroller.getFinalY() < mMinFlingY) {
                    mScroller.setFinalY(mMinFlingY);
                    Log.d(TAG, "onTouchEvent: mmScroller.getFinalY() < mMinFlingY:" + mScroller.getFinalY() + "," + mMinFlingY);
                }
                //mHandler.post(mScrollerRunnable);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mItemHeight / 2) {
            if (mScrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemHeight = mDrawnRect.height() / 5;
        mFirstItemDrawX = mDrawnRect.centerX();
        mFirstItemDrawY = (int) ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2);//计算绘制第一个item时，画笔的Y坐标
        //中间的Item边框
        mSelectedItemRect.set(getPaddingLeft(), mItemHeight * mHalfVisibleItemCount,
                getWidth() - getPaddingRight(), mItemHeight + mItemHeight * mHalfVisibleItemCount);
        mCenterItemDrawnY = mFirstItemDrawY + mItemHeight * mHalfVisibleItemCount;

        mScrollOffsetY = -mItemHeight * mCurrentPosition;
        computeFlingLimitY();
    }

    private void computeFlingLimitY() {
        mMinFlingY = -mItemHeight * (mData.size() - 1);//向上滚动最大的偏移量
        mMaxFlingY = 0;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollOffsetY = mScroller.getCurrY();
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: mScroller.getFinalY()=" + mScroller.getFinalY());

        mPaint.setTextAlign(Paint.Align.CENTER);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCurtainColor);
        canvas.drawRect(mSelectedItemRect, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCurtainBorderColor);
        canvas.drawRect(mSelectedItemRect, mPaint);
        canvas.drawRect(mDrawnRect, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        int drawnSelectedPos = -mScrollOffsetY / mItemHeight;
        for (int drawDataPos = drawnSelectedPos - mHalfVisibleItemCount - 1;
             drawDataPos <= drawnSelectedPos + mHalfVisibleItemCount + 1; drawDataPos++) {
            int position = drawDataPos;
            if (position < 0 || position > mData.size() - 1) {
                continue;
            }
            //在中间位置的Item作为被选中的。
            if (drawnSelectedPos == drawDataPos) {
                mPaint.setColor(getResources().getColor(R.color.colorAccent));
            } else {
                mPaint.setColor(Color.BLACK);
            }

            String data = mData.get(position);
            int itemDrawY = mFirstItemDrawY + (drawDataPos + mHalfVisibleItemCount) * mItemHeight + mScrollOffsetY;
            canvas.drawText(data, mFirstItemDrawX, itemDrawY, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mItemHeight = getMeasuredHeight() / 5;//默认画5条数据，每个item的高度就是总的高度/5
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int deltaX = destX - scrollX;
        int scrollY = getScrollY();
        int deltaY = destY - scrollY;
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY, 1000);
        invalidate();
    }
}
