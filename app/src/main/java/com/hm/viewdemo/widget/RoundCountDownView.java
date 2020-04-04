package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
        backgroundColor = context.getResources().getColor(R.color.colorAccent);
        arcColor = context.getResources().getColor(R.color.white);
        mPaint.setColor(backgroundColor);

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
        int measuredHeight = getMeasuredHeight();

        rectF.set(0, 0, measuredWidth, measuredHeight);
        mPaint.setColor(backgroundColor);
        canvas.drawRoundRect(rectF, 40, 40, mPaint);

        testPathArcTo(canvas);

        mPaint.setColor(arcColor);

        rectF.set(-50, -50, measuredWidth + 50, measuredHeight + 50);
        canvas.drawArc(rectF, startAngle, swipeAngle, true, mPaint);


        if (swipeAngle > 0) {
            startAngle++;
            swipeAngle--;
            postDelayed(action, 20);
        }
    }


    private RectF clipPathRectF = new RectF();

    /**
     * 测试 path.arcTo
     *
     * @param canvas
     */
    private void testPathArcTo(Canvas canvas) {
        //移动到屏幕中间
        mPaint.setColor(Color.BLACK);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();


        //canvas.translate(width / 2.0f, height / 2.0f)

        Path path = new Path();

        //左上角弧形
        clipPathRectF.set(0f, 0f, 80f, 80f);
        path.arcTo(clipPathRectF, -180f, 90f);

        path.lineTo(measuredWidth - 80f, 0f);

        //右上角弧形
        clipPathRectF.set(measuredWidth - 80f, 0f, measuredWidth * 1.0f, 80f);
        path.arcTo(clipPathRectF, -90f, 90f);

        path.lineTo(measuredWidth * 1.0f, measuredHeight - 80f);

        //右下角弧形
        clipPathRectF.set(measuredWidth - 80f, measuredHeight - 80f, measuredWidth * 1.0f, measuredHeight * 1.0f);
        path.arcTo(clipPathRectF, 0f, 90f);

        path.lineTo(80f, measuredHeight * 1.0f);

        //左下角弧形
        clipPathRectF.set(0f, measuredHeight - 80f, 80f, measuredHeight * 1.0f);
        path.arcTo(clipPathRectF, 90f, 90f);

        canvas.clipPath(path);
        //canvas.drawPath(path,mPaint);
        //val oval = RectF(0f, 0f, 300f, 300f)
        //path.arcTo(oval, 0f, 270f)
        // canvas.drawPath(path, mPaint)
    }

}
