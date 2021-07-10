package com.capton.colorfulprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by dumingwei on 2019-08-13.
 * Desc:
 */
public class RoundLayoutTwoHello extends RelativeLayout {

    private static final String TAG = "RoundLayoutTwo";
    // 1. 定义圆角信息 和 path
    private float[] radii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private int mRoundCorner;
    private Paint paint;
    private RectF mRectF;
    private Path mPath;

    public RoundLayoutTwoHello(Context context) {
        this(context, null);
    }

    public RoundLayoutTwoHello(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLayoutTwoHello);
        mRoundCorner = ta.getDimensionPixelSize(R.styleable.RoundLayoutTwoHello_two_round_corner, 0);
        ta.recycle();

        Log.i(TAG, "RoundLayoutTwo: mRoundCorner=" + mRoundCorner);
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

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
        mPath.addRoundRect(mRectF, radii, Path.Direction.CW);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()),
                null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawPath(mPath, paint);
        canvas.restore();
    }
}
