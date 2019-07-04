package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/2/26.
 */
public class TestSmoothScrollView extends View {

    private final String TAG = getClass().getSimpleName();

    private int mScaledTouchSlop;
    //分别记录上次滑动的坐标
    private int mLastX = 0;
    private int mLastY = 0;
    private Scroller scroller;
    private Paint paint = new Paint();
    private int color;

    public TestSmoothScrollView(Context context) {
        this(context, null);
    }

    public TestSmoothScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public TestSmoothScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        scroller = new Scroller(getContext());
        Log.d(TAG, "sts:" + mScaledTouchSlop);
        color = getContext().getResources().getColor(R.color.colorAccent);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: " + getLeft() + "," + getRight() + "," + getTop() + "," + getBottom());
        canvas.drawColor(color);
        paint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        canvas.drawRect(0, 0, getWidth()/2f, getHeight()/2f, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "move, deltaX:" + deltaX + " deltaY:" + deltaY);
                int translationX = (int) (getTranslationX() + deltaX);
                int translationY = (int) (getTranslationY() + deltaY);
                setTranslationX(translationX);
                setTranslationY(translationY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;*/
        return true;
    }

    public void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();

        int deltaX = destX - scrollX;
        scroller.startScroll(scrollX, 0, deltaX, 0, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //scroller.computeScrollOffset()会根据时间的流逝的百分比计算处scrollX和scrollY改变的百分比并计算出当前的值。
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
