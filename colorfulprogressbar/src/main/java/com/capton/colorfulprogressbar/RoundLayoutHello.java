package com.capton.colorfulprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by dumingwei on 2019-08-13.
 * Desc:
 */
public class RoundLayoutHello extends FrameLayout {

    // 1. 定义圆角信息 和 path
    private float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private Path mPath;
    private int mRoundCorner;
    private RectF mRectF;

    public RoundLayoutHello(Context context) {
        this(context, null);
    }

    public RoundLayoutHello(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLayoutHello);
        mRoundCorner = ta.getDimensionPixelSize(R.styleable.RoundLayoutHello_round_corner, 0);
        ta.recycle();
        radii[0] = mRoundCorner;
        radii[1] = mRoundCorner;
        radii[2] = mRoundCorner;
        radii[3] = mRoundCorner;
        radii[4] = mRoundCorner;
        radii[5] = mRoundCorner;
        radii[6] = mRoundCorner;
        radii[7] = mRoundCorner;
        mRectF = new RectF();
        mPath = new Path();
    }

    public void setRoundCorner(int mRoundCorner) {
        this.mRoundCorner = mRoundCorner;
        radii[0] = mRoundCorner;
        radii[1] = mRoundCorner;
        radii[2] = mRoundCorner;
        radii[3] = mRoundCorner;
        radii[4] = mRoundCorner;
        radii[5] = mRoundCorner;
        radii[6] = mRoundCorner;
        radii[7] = mRoundCorner;
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
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        // 绘制圆角
        canvas.clipPath(mPath);
        return super.drawChild(canvas, child, drawingTime);
    }

}
