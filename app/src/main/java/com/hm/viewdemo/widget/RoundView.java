package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dumingwei on 2017/3/4.
 * 圆角矩形，只有左上角和右上角是圆角
 */
public class RoundView extends View {

    private int mColor;
    private int bgColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;
    private RectF leftTopRectF;
    private RectF rightTopRectF;
    private PorterDuffXfermode xfermode;// 图形混合模式

    public RoundView(Context context) {
        this(context, null);
    }

    public RoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColor = Color.parseColor("#00000000");
        bgColor = Color.parseColor("#f6f6f6");
        leftTopRectF = new RectF();
        rightTopRectF = new RectF();
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(bgColor);
        leftTopRectF.set(0, 0, getHeight() * 2, getHeight() * 2);
        rightTopRectF.set(getWidth() - getHeight() * 2, 0, getWidth(), getHeight() * 2);
        //新建一个layer,放置在canvas默认layer的上部，产生的layer初始时是完全透明的
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //左上角的正方形
        canvas.drawRect(0, 0, getHeight(), getHeight(), mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(mColor);
        //左上角扇形
        canvas.drawArc(leftTopRectF, -90, -90, true, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();
        mPaint.setColor(bgColor);
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRect(getWidth() - getHeight(), 0, getWidth(), getHeight(), mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(mColor);
        canvas.drawArc(rightTopRectF, 0, -90, true, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        postInvalidate();
    }
}
