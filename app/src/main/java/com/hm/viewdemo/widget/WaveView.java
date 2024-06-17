package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dumingwei on 2020/12/13
 * <p>
 * Desc:
 * 不具有通用性
 * 整个View构成逆时针：
 * <p>
 * V<<<<<<<<<< ^
 * V           ^
 * V           ^
 * V           ^
 * >>>>>>>>>>>>>
 */
public class WaveView extends View {

    private static final String TAG = "SubscribeButtonWaveView";

    /**
     * 半径应该和包裹的按钮的弧度一样
     */
    private int mRadius; // 圆心离左右边距
    private int dp16;
    private int mMaxPlayCount = 3 * 5 - 1;
    private int mPlayCount = 0;
    private boolean mIsRunning = false;
    private Paint mPaint;
    private Paint mRimPaint;
    private long mDuration = 3000; // 一个波纹从创建到消失的持续时间
    private int mSpeed = 1500;   // 波纹的创建速度，每 mSpeed ms创建一个
    //如果是匀速运动的效果其实不需要这个差值器
    private Interpolator mInterpolator = new LinearInterpolator();
    private long mLastCreateTime;
    private List<Rim> mRimList = new ArrayList<>();

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRadius = ScreenUtil.dpToPx(context, 29);
        dp16 = ScreenUtil.dpToPx(context, 16);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(context.getColor(R.color.colorAccent));

        mRimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRimPaint.setStyle(Paint.Style.STROKE);
        //绘制最外层的边框，1像素
        mRimPaint.setColor(Color.parseColor("#66FFFFFF"));
        mRimPaint.setStrokeWidth(1);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                /*if (mPlayCount > mMaxPlayCount) {
                    mIsRunning = false;
                    return;
                }*/
                int mod = mPlayCount % 5;
                if (mod == 3 || mod == 4) {
                    //创建了不绘制
                    newRim(true);
                } else {
                    newRim(false);
                }
                mPlayCount++;
                postDelayed(mRunnable, mSpeed);
            }
        }
    };

    public void setMaxPlayCount(int playCount) {
        mMaxPlayCount = playCount;
    }

    public void start() {
        if (!mIsRunning) {
            mIsRunning = true;
            mRunnable.run();
        }
    }

    public void resume() {
        stop();
        start();
    }

    public void stop() {
        mIsRunning = false;
        removeCallbacks(mRunnable);
    }

    private void newRim(boolean noDraw) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Rim rim = new Rim();
        rim.noDraw = noDraw;
        mRimList.add(rim);
        invalidate();
        mLastCreateTime = currentTime;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");
        Iterator<Rim> iterator = mRimList.iterator();
        while (iterator.hasNext()) {
            Rim rim = iterator.next();
            if (System.currentTimeMillis() - rim.mCreateTime < mDuration && !rim.noDraw) {
                int alpha = rim.getAlpha();
                float radius = rim.getRidus();
                mPaint.setAlpha(alpha);
                mRimPaint.setAlpha(alpha);
                drawRim(canvas, radius);
            } else {
                //超过了mDuration移除
                iterator.remove();
            }
        }
        if (mRimList.size() > 0) {
            postInvalidateDelayed(50);
        }
    }

    private void drawRim(Canvas canvas, float radius) {
        Log.d(TAG, "drawRim: radius = " + radius);
        float offset = mRadius - radius;
        int height = getHeight();
        int width = getWidth();

        Path path = new Path();
        RectF rectF = new RectF(offset, offset, mRadius * 2 - offset, height - offset);
        //左半圆从-90度(第3象限到底4象限)开始，扫过的角度是180度
        path.arcTo(rectF, -90, -180);
        //一条直线
        path.lineTo(width - mRadius, height - offset);
        //右半圆
        RectF rectF2 = new RectF(width - mRadius - radius, offset, width - offset, height - offset);
        path.arcTo(rectF2, 90, -180);
        //封闭从而构成一个直线。
        path.close();
        canvas.drawPath(path, mPaint);
        canvas.drawPath(path, mRimPaint);
    }

    private class Rim {
        private long mCreateTime;
        private boolean noDraw;

        Rim() {
            this.mCreateTime = System.currentTimeMillis();
        }

        /**
         * percent 越大，越透明；percent等于1的时候完全透明
         *
         * @return
         */
        public int getAlpha() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            float interpolation = mInterpolator.getInterpolation(percent);
            Log.d(TAG, "getAlpha: percent = " + percent + ",interpolation = " + interpolation);

            //return (int) ((1.0f - interpolation) * 255 / 2);
            return (int) ((1.0f - percent) * 255 / 2);
            //return (int) ((1.0f - percent) * 255);
        }

        public float getRidus() {
            //过去的时间
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            //根据过去的时间
            float interpolation = mInterpolator.getInterpolation(percent);

            Log.d(TAG, "getRidus: percent = " + percent + ",interpolation = " + interpolation);

            //return interpolation * (mRadius - dp16) + dp16;
            //当percent为1的时候半径最大是 mRadius
            return percent * (mRadius - dp16) + dp16;
        }
    }
}
