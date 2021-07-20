package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
 * 圆形遮罩层，动态改变遮罩的大小
 * <p>
 * 1. Canvas是比控件的宽高要大的。
 * 2. 创建源Bitmap，是为了让整个源图层有像素。
 * 3. 别忘了在构造函数中设置 setLayerType(View.LAYER_TYPE_SOFTWARE, null);
 */
public class CircleMaskView extends View {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int DEFAULT_SIZE = 200;
    private PorterDuffXfermode dstInXfermode;// 图形混合模式
    private PorterDuffXfermode clearMode;// 图形混合模式

    private static final String TAG = "CircleMaskView";

    private int innerRadius;

    //原图片的开始的半径
    private int srcCanvasRadius;

    //目标图层
    private Bitmap dstBitmap;
    //源图层
    private Bitmap srcBitmap;
    private final Paint srcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Canvas srcCanvas;

    public void setSrcCanvasRadius(int srcCanvasRadius) {
        this.srcCanvasRadius = srcCanvasRadius;
    }

    public CircleMaskView(Context context) {
        this(context, null);
    }

    public CircleMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CircleMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0f);
        //这行代码一定要加，不然 xfermode 会出现不可预料的效果
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        dstInXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.scenery);
        int width = temp.getWidth();
        int height = temp.getHeight();

        int newWidth = ScreenUtil.dpToPx(getContext(), 249);
        int newHeight = ScreenUtil.dpToPx(getContext(), 289);

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

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

        Log.i(TAG, "onMeasure: " + getMeasuredWidth() + " " + getMeasuredHeight());
        //源图片
        srcBitmap = makeSrc();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(dstBitmap, 0, 0, mPaint);
        mPaint.setXfermode(dstInXfermode);
        canvas.drawBitmap(makeSrc(), 0, 0, mPaint);
        mPaint.setXfermode(null);
    }

    private Bitmap makeSrc() {
        if (srcBitmap == null) {
            //直径
            int diameter = srcCanvasRadius * 2;
            srcBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        }

        if (srcCanvas == null) {
            srcCanvas = new Canvas(srcBitmap);
        }

        Log.i(TAG, "makeSrc: srcCanvasRadius = " + srcCanvasRadius + " getMeasuredWidth()  = " + getMeasuredWidth() + " getMeasuredHeight() = " + getMeasuredHeight());

        //清除源图层上的像素，这个一定要清除
        srcPaint.setXfermode(clearMode);
        srcCanvas.drawPaint(srcPaint);
        srcPaint.setXfermode(null);
        //设置的透明度是FF
        srcPaint.setColor(0xFFFFFFFF);
        //重新在源图层上绘制圆形，圆形的半径是不断缩小的，圆心是控件的中心点
        srcCanvas.drawCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, innerRadius, srcPaint);
        return srcBitmap;
    }

    public void setInnerRadius(int radius) {
        innerRadius = radius;
    }

    public void setInnerRadiusAndInValidate(int radius) {
        innerRadius = radius;
        invalidate();
    }

    public void setColor(int mColor) {
        mPaint.setColor(mColor);
        invalidate();
    }

}
