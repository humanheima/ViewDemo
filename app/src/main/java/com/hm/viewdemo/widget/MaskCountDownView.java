package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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
public class MaskCountDownView extends View {

    private static final String TAG = "MaskCountDownView";

    private static final int DEFAULT_SIZE = 200;

    private final float THRESHOLD = 0.001f;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF rectF;
    private RectF clipPathRectF;
    private Path path;

    private float sweepAngle;
    private float startAngle;
    /**
     * 当startAngle大于等于finishAngle的时候要结束，最多转一圈
     */
    private float finishAngle;

    private int maskColor;

    //单位是秒
    private int stageTime;

    /**
     * 圆角大小的默认值，单位是px
     */
    private static final int BORDER_RADIUS_DEFAULT = 30;
    private int cornerRadius;

    public MaskCountDownView(Context context) {
        this(context, null);
    }

    public MaskCountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskCountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //宽高最大值
        int sideLength = Math.max(width, height);
        //正方形外接圆形的半径
        /**
         * 外接圆的半径
         */
        float outCircleR = (float) (sideLength * Math.sqrt(2) / 2);
        //绘制扇形时候需要增加的偏移量
        //绘制扇形时候需要增加的偏移量
        float offset = outCircleR - sideLength / 2f;
        Log.d(TAG, "onMeasure: width = " + width + " , height = " + height +
                " ,outCircleR = " + outCircleR + ", offset = " + offset);
        rectF.set(-offset, -offset, width + offset, height + offset);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (finished()) {
            return;
        }
        clipPath(canvas);
        mPaint.setColor(maskColor);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, mPaint);
    }

    /**
     * 结束角度固定是270度，起始角度要根据计算结束时间和开始时间的来计算
     *
     * @param stageTime             整个阶段时间，单位是毫秒
     * @param toArriveThisStageTime 到达当前阶段还需要听多长时间 ，单位是毫秒
     */
    public void setInitialTimes(int stageTime, int toArriveThisStageTime) {
        this.stageTime = stageTime;
        //270度是
        finishAngle = 270f;
        //还需要走这么多角度
        sweepAngle = (toArriveThisStageTime * 1.0f / stageTime) * 360;
        startAngle = finishAngle - sweepAngle;
        Log.d(TAG, "setStageAndFinishTime: startAngle = " + startAngle);
        invalidate();
    }

    public void countDown(int currentTime) {
        if (finished()) {
            Log.d(TAG, "countDown: finished");
            return;
        }
        //当前转过的角度
        sweepAngle = (currentTime * 1.0f / stageTime) * 360;

        startAngle = finishAngle - sweepAngle;

        Log.d(TAG, "countDown: startAngle = " + startAngle);

        invalidate();
    }


    /**
     * 倒计时是否结束
     *
     * @return
     */
    public boolean finished() {
        return (finishAngle - startAngle) <= THRESHOLD;
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
