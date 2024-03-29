package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/3/4.
 */
public class CircleView extends View {


    private static final String TAG = "CircleView";

    private int mColor;
    private int bgColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mColor = array.getColor(R.styleable.CircleView_circle_color, Color.RED);
        bgColor = array.getColor(R.styleable.CircleView_bg_color, Color.BLUE);
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecModel == MeasureSpec.AT_MOST && heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE);
        } else if (widthSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightSpecSize);
        } else if (heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, DEFAULT_SIZE);
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.i(TAG, "layout: ");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout: ");
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    public void draw(Canvas canvas) {
        Log.i(TAG, "draw: ");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: ");
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int radius = Math.min(width, height) / 2;
        mPaint.setColor(bgColor);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        //mPaint.setColor(mColor);
        //canvas.drawCircle(paddingLeft + width / 2f, paddingTop + height / 2f, radius, mPaint);
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        requestLayout();
        //postInvalidate();
    }

}

