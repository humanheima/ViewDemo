package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2019-08-13.
 * Desc:
 */
public class RoundLayout extends FrameLayout {


    // 1. 定义圆角信息 和 path
    private float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private Path mPath;
    private int mRoundCorner;
    private RectF mRectF;


    public RoundLayout(Context context) {
        this(context, null);
    }

    public RoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout);
        mRoundCorner = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_corner, 0);
        radii[0] = mRoundCorner;
        radii[1] = mRoundCorner;
        radii[3] = mRoundCorner;
        radii[4] = mRoundCorner;
        radii[5] = mRoundCorner;
        radii[6] = mRoundCorner;
        radii[7] = mRoundCorner;
        mRectF = new RectF();
        mPath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.left = getPaddingLeft();
        mRectF.top = getPaddingTop();
        mRectF.right = w - getPaddingRight();
        mRectF.bottom = h - getPaddingBottom();
        mPath.addRoundRect(mRectF, radii, Path.Direction.CW);
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        // 绘制圆角
        canvas.clipPath(mPath);
        super.dispatchDraw(canvas);
    }

}
