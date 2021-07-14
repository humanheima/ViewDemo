package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/3/4.
 * 圆角矩形，只有左上角和右上角是圆角
 */
public class CircleMaskView extends View {

    private int mColor;
    private int mFrontColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;
    private PorterDuffXfermode xfermode;// 图形混合模式

    private float mPaintWidth = 1f;

    private static final String TAG = "CircleMaskView";

    private float innerRadius;
    private float radius;


    public CircleMaskView(Context context) {
        this(context, null);
    }

    public CircleMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColor = getResources().getColor(R.color.colorAccent);
        mFrontColor = getResources().getColor(R.color.colorPrimary);
        mPaint.setColor(mColor);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);
        //这行代码一定要加，不然 xfermode 会出现不可预料的效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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

        radius = getMeasuredWidth() / 2f;
        innerRadius = radius;
        Log.i(TAG, "onMeasure: " + getMeasuredWidth() + " " + getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.BLUE);
        float cx = getMeasuredWidth() / 2f;
        float cy = getMeasuredHeight() / 2f;
        float radius = getMeasuredWidth() / 2f;

        mPaint.setXfermode(null);
        canvas.drawCircle(cx, cy, radius, mPaint);
        mPaint.setXfermode(xfermode);
        mPaint.setColor(mFrontColor);
        canvas.drawCircle(cx, cy, innerRadius, mPaint);
    }

    public void setInnerRadius(int radius) {
        innerRadius = radius;
        invalidate();
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        invalidate();
    }
}
