package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by xmly on 2020/4/2.
 * <p>
 * Desc:
 */
public class RoundCountDownView extends View {


    private static final int DEFAULT_SIZE = 200;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF rectF;

    private int swipeAngle = 360;
    private int startAngle = -90;
    private Runnable action;
    private int backgroundColor;
    private int roundRecColor;

    private int arcColor;

    public RoundCountDownView(Context context) {
        this(context, null);
    }

    public RoundCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        backgroundColor = context.getResources().getColor(R.color.colorPrimary);
        roundRecColor = context.getResources().getColor(R.color.colorAccent);
        mPaint.setColor(roundRecColor);
        rectF = new RectF();
        action = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
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
        super.onDraw(canvas);
        int measuredWidth = getMeasuredWidth();
        rectF.set(0, 0, measuredWidth, measuredWidth);
        mPaint.setColor(backgroundColor);
        canvas.drawRect(rectF, mPaint);
        mPaint.setColor(roundRecColor);
        /*float left = (float) (measuredWidth - (measuredWidth / Math.sqrt(2))) / 2f;
        rectF.set(left, left, measuredWidth, measuredWidth);
        canvas.drawRoundRect(rectF, 16, 16, mPaint);*/

        mPaint.setColor(roundRecColor);
        float left = (float) (measuredWidth - (measuredWidth / Math.sqrt(2))) / 2f;
        float right = measuredWidth - left;
        rectF.set(left, left, right, right);
        //canvas.drawRoundRect(rectF, 16, 16, mPaint);
        canvas.drawRect(rectF, mPaint);

        arcColor = getResources().getColor(R.color.white);
        mPaint.setColor(arcColor);
        rectF.set(0, 0, measuredWidth, measuredWidth);
        canvas.drawArc(rectF, startAngle, swipeAngle, true, mPaint);


        if (swipeAngle > 0) {
            startAngle++;
            swipeAngle--;
            postDelayed(action, 20);
        }
        //canvas.drawArc(rectF, 0, 90, true, mPaint);
    }
}
