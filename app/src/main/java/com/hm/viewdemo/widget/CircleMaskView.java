package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by dumingwei on 2017/3/4.
 */
public class CircleMaskView extends View {

    private int mColor;
    private int mFrontColor;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;
    private PorterDuffXfermode xfermode;// 图形混合模式
    private PorterDuffXfermode clear;// 图形混合模式

    private static final String TAG = "CircleMaskView";

    private int innerRadius;

    //原图片的开始的半径
    private int srcCanvasRadius;
    //src需要平移这段距离以后，开始画圆
    private int translateXDistance;
    private int translateYDistance;

    private float radius;

    private Bitmap dstBitmap;


    private Bitmap srcBitmap;
    private Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Canvas canvas;

    public void setSrcCanvasRadius(int srcCanvasRadius) {
        this.srcCanvasRadius = srcCanvasRadius;
    }

    public void setTranslateXDistance(int translateXDistance) {
        this.translateXDistance = translateXDistance;
    }

    public void setTranslateYDistance(int translateYDistance) {
        this.translateYDistance = translateYDistance;
    }

    public CircleMaskView(Context context) {
        this(context, null);
    }

    public CircleMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CircleMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mColor = getResources().getColor(R.color.colorAccent);
        //mFrontColor = getResources().getColor(Color.TRANSPARENT);
        mFrontColor = Color.parseColor("#d8000000");
        mPaint.setColor(mColor);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0f);
        //这行代码一定要加，不然 xfermode 会出现不可预料的效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        clear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        //dstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        //dstBitmap = ImageUtil.getSampledBitmapFromResource(getResources(), R.drawable.scenery, ScreenUtil.dpToPx(getContext(), 249), ScreenUtil.dpToPx(getContext(), 289));
        int width = temp.getWidth();
        int height = temp.getHeight();

        int newWidth = ScreenUtil.dpToPx(getContext(), 249);
        int newHeight = ScreenUtil.dpToPx(getContext(), 289);

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        //dstBitmap = Bitmap.createBitmap(temp, 0, 0, newWidth, newHeight);
        dstBitmap = Bitmap.createBitmap(temp, 0, 0, width, height, matrix, true);

        temp.recycle();
        //目标图片
        Log.i(TAG, "CircleMaskView: " + temp.getWidth() + " height = " + temp.getHeight() + " size = " + temp.getByteCount() / 1024 / 1024 + "MB");
        Log.i(TAG, "CircleMaskView: " + dstBitmap.getWidth() + " height = " + dstBitmap.getHeight() + " size = " + dstBitmap.getByteCount() / 1024 / 1024 + "MB");

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


        //dstBitmap.setWidth(getMeasuredWidth());
        // dstBitmap.setHeight(getMeasuredHeight());
        //源图片
        srcBitmap = makeSrc();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.BLUE);
        canvas.drawBitmap(dstBitmap, 0, 0, mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(makeSrc(), 0, 0, mPaint);
        mPaint.setXfermode(null);
    }

    private Bitmap makeSrc() {
        if (srcBitmap == null) {
            //srcBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            //直径
            int diameter = srcCanvasRadius * 2;
            srcBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        }

        if (canvas == null) {
            canvas = new Canvas(srcBitmap);
            //canvas.translate(-translateXDistance, -translateYDistance);
        }


        Log.i(TAG, "makeSrc: srcCanvasRadius = " + srcCanvasRadius + " getMeasuredWidth()  = " + getMeasuredWidth() + " getMeasuredHeight() = " + getMeasuredHeight());

        //Canvas c = new Canvas(srcBitmap);

        p.setXfermode(clear);
        canvas.drawPaint(p);
        p.setXfermode(null);
        //设置的透明度是FF
        p.setColor(0xFFFFFFFF);
        //canvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, (1.5f * innerRadius / 2f), p);
        //canvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, innerRadius, p);
        canvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, innerRadius, p);
        return srcBitmap;
    }

    public void setInnerRadius(int radius) {
        innerRadius = radius;
    }

    public void setInnerRadiusAndInValidate(int radius) {
        innerRadius = radius;
        //srcBitmap = makeSrc();
        invalidate();

    }


    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaint.setColor(mColor);
        invalidate();
    }
}
