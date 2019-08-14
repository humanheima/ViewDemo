package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.util.ScreenUtil;

/**
 * Crete by dumingwei on 2019-08-14
 * Desc:
 */
public class XFermodeView extends View {


    private static final String TAG = "XFermodeView";

    private static final int HORIZONTAL_SPACE = 36;
    private static final int VERTICAL_SPACE = 72;
    private static final int ROW_MAX = 4;   // number of samples per row
    private static final int DEFAULT_SIZE = 2000;

    private Bitmap mSrcB;
    private Bitmap mDstB;
    private Shader mBG;     // background checker-board pattern
    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };
    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    private int itemWidth = 0;
    private Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint = new Paint();

    public XFermodeView(Context context) {
        this(context, null);
    }

    public XFermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        labelP.setTextAlign(Paint.Align.CENTER);
        labelP.setTextSize(ScreenUtil.spToPx(getContext(), 14));
        paint.setFilterBitmap(false);

        itemWidth = (ScreenUtil.getScreenWidth(getContext()) - 5 * HORIZONTAL_SPACE) / ROW_MAX;

        mSrcB = makeSrc(itemWidth, itemWidth);
        mDstB = makeDst(itemWidth, itemWidth);

        // make a ckeckerboard pattern 下面这几行代码可以注释掉，不影响
        Bitmap bm = Bitmap.createBitmap(new int[]{0xFFFFFFFF, 0xFFCCCCCC,
                        0xFFCCCCCC, 0xFFFFFFFF}, 2, 2,
                Bitmap.Config.RGB_565);
        mBG = new BitmapShader(bm, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        mBG.setLocalMatrix(m);
    }

    // create a bitmap with a circle, used for the "dst" image
    Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4f, h * 3 / 4f), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p);
        return bm;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecModel = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "onMeasure: " + widthSpecSize + "," + heightSpecSize);
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
        Log.d(TAG, "onDraw: " + getMeasuredHeight() + "," + getMeasuredWidth());
        canvas.drawColor(Color.WHITE);
        canvas.translate(HORIZONTAL_SPACE, VERTICAL_SPACE);
        int x = 0;
        int y = 0;
        for (int i = 0; i < sModes.length; i++) {
            // draw the border
            paint.setStyle(Paint.Style.STROKE);
            paint.setShader(null);
            canvas.drawRect(x - 0.5f, y - 0.5f, x + itemWidth + 0.5f, y + itemWidth + 0.5f, paint);

            // draw the checker-board pattern，这三行代码可以去掉，不影响
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(mBG);
            canvas.drawRect(x, y, x + itemWidth, y + itemWidth, paint);

            // draw the src/dst example into our offscreen bitmap
            int sc = canvas.saveLayer(x, y, x + itemWidth, y + itemWidth, null, Canvas.ALL_SAVE_FLAG);
            canvas.translate(x, y);
            canvas.drawBitmap(mDstB, 0, 0, paint);
            paint.setXfermode(sModes[i]);
            canvas.drawBitmap(mSrcB, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(sc);

            // draw the label
            canvas.drawText(sLabels[i], x + itemWidth / 2f, y - labelP.getTextSize() / 2, labelP);

            x += itemWidth + HORIZONTAL_SPACE;
            // wrap around when we've drawn enough for one row
            if ((i % ROW_MAX) == ROW_MAX - 1) {
                x = 0;
                y += itemWidth + VERTICAL_SPACE;
            }
        }
    }
}
