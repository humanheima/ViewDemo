package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/11/30 0030.
 */

public class PorterDuffXfermodeView extends AppCompatImageView {

    private Xfermode xfermode;
    private Paint paint;
    //先绘制的是dst
    private Bitmap srcBitmap, dstBitmap;
    private RectF dstRect, srcRect;

    public PorterDuffXfermodeView(Context context) {
        this(context, null);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        //xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST);
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.src);
        dstBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dst);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        //绘制目标图
        paint.setXfermode(null);
        canvas.drawBitmap(dstBitmap, null, dstRect, paint);
        paint.setXfermode(xfermode);
        //绘制源图
        canvas.drawBitmap(srcBitmap, null, srcRect, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w / 2;
        int centerY = h / 2;
        int quarterWidth = width / 4;
        srcRect = new RectF(centerX - quarterWidth, centerY - quarterWidth, centerX + quarterWidth, centerY + quarterWidth);
        dstRect = new RectF(centerX - quarterWidth, centerY - quarterWidth, centerX + quarterWidth, centerY + quarterWidth);
    }
}
