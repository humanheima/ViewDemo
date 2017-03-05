package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/3/4.
 */
public class CircleView extends View {

    private int mColor;
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
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = array.getColor(R.styleable.CircleView_circle_color, Color.RED);
        array.recycle();
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecModel = MeasureSpec.getMode(heightMeasureSpec);
        if (widthSpecModel == MeasureSpec.AT_MOST && heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, DEFAULT_SIZE);
        } else if (widthSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_SIZE, heightMeasureSpec);
        } else if (heightSpecModel == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecModel, DEFAULT_SIZE);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop + height / 2, radius, mPaint);
    }
}
