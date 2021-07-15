package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/3/4.
 */
public class CircleMaskViewTwo extends View {

    private int mColor;
    private int mFrontColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;
    private PorterDuffXfermode xfermode;// 图形混合模式

    private static final String TAG = "CircleMaskView";

    private int innerRadius;
    private float radius;

    private Bitmap dstBitmap;

    private Bitmap srcBitmap;


    public CircleMaskViewTwo(Context context) {
        this(context, null);
    }

    public CircleMaskViewTwo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CircleMaskViewTwo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColor = Color.parseColor("#d8000000");
        //mFrontColor = getResources().getColor(Color.TRANSPARENT);
        mFrontColor = Color.parseColor("#d8000000");
        mPaint.setColor(mColor);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0f);
        //这行代码一定要加，不然 xfermode 会出现不可预料的效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        //目标图片
        dstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        Log.i(TAG, "CircleMaskView: " + dstBitmap.getWidth() + " height = " + dstBitmap.getHeight());
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

        radius = getMeasuredHeight() / 2f;
        Log.i(TAG, "onMeasure: " + getMeasuredWidth() + " " + getMeasuredHeight());
        //源图片
        //srcBitmap = makeSrc();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(mColor);
        //canvas.drawBitmap(dstBitmap, 0, 0, mPaint);
        //mPaint.setXfermode(xfermode);
       // canvas.drawBitmap(makeSrc(), 0, 0, mPaint);
        //mPaint.setXfermode(null);
    }

    private Bitmap makeSrc() {
        Bitmap bm = Bitmap.createBitmap(innerRadius, innerRadius, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置的透明度是FF
        p.setColor(0xFFFFFFFF);
        c.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, innerRadius, p);
        return bm;
    }

    public void setInnerRadius(int radius) {
        innerRadius = radius;

    }

    public void setInnerRadiusAndInValidate(int radius) {
        //innerRadius = radius;
        //invalidate();

    }


    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        invalidate();
    }
}
