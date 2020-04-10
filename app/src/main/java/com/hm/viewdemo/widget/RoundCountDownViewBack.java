package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2020/4/2.
 * <p>
 * Desc:
 */
public class RoundCountDownViewBack extends View {

    private static final String TAG = "RoundCountDownView";

    private static final int DEFAULT_SIZE = 200;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF rectF;
    private RectF clipPathRectF;
    private Path path;

    private int sweepAngle;
    private int startAngle;

    /**
     * 当startAngle大于等于finishAngle的时候要结束，最多转一圈
     */
    private int finishAngle;
    private int maskColor;

    /**
     * 圆角大小的默认值，单位是px
     */
    private static final int BORDER_RADIUS_DEFAULT = 30;
    private int cornerRadius;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    public RoundCountDownViewBack(Context context) {
        this(context, null);
    }

    public RoundCountDownViewBack(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCountDownViewBack(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaskCountDownView);
        cornerRadius = a.getDimensionPixelSize(R.styleable.MaskCountDownView_cornerRadius, BORDER_RADIUS_DEFAULT);

        startAngle = a.getInt(R.styleable.MaskCountDownView_startAngle, 0);
        finishAngle = a.getInt(R.styleable.MaskCountDownView_finishAngle, 0);

        sweepAngle = finishAngle - startAngle;

        maskColor = a.getColor(R.styleable.MaskCountDownView_maskColor, Color.TRANSPARENT);

        Log.d(TAG, "init: cornerRadius = " + cornerRadius + "，startAngle " + startAngle + "，swipeAngle = " + sweepAngle);
        a.recycle();

        rectF = new RectF();
        clipPathRectF = new RectF();
        path = new Path();
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
        if (finished()) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();


        clipPath(canvas);

        mPaint.setColor(maskColor);
        rectF.set(-50, -50, measuredWidth + 50, measuredHeight + 50);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint);
    }

    public void setStartAndFinishAngle(int startAngle, int finishAngle) {
        this.startAngle = startAngle;
        this.finishAngle = finishAngle;
        sweepAngle = finishAngle - startAngle;
    }

    public void countDown() {
        if (finished()) {
            Log.d(TAG, "countDown: finished");
            return;
        }
        startAngle += 1;
        sweepAngle -= 1;
        invalidate();
    }


    /**
     * 是否已经走过一圈了
     *
     * @return
     */
    public boolean finished() {
        return startAngle >= finishAngle;
    }

    /**
     * 将画布剪裁成圆角矩形
     *
     * @param canvas
     */
    private void clipPath(Canvas canvas) {
        //移动到屏幕中间
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        path.reset();

        clipPathRectF.set(0, 0, measuredWidth, measuredHeight);
        path.addRoundRect(clipPathRectF, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(path);
    }

}
