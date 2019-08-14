package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by dumingwei on 2017/11/30 0030.
 */

public class PorterDuffXfermodeView extends AppCompatImageView {

    private Paint paint;

    private Bitmap mSrcB;
    private Bitmap mDstB;

    private Xfermode[] sModes = {
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
    private int itemWidth;

    public PorterDuffXfermodeView(Context context) {
        this(context, null);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //加上这行代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        itemWidth = ScreenUtil.dpToPx(context, 200);
        mSrcB = makeSrc(itemWidth, itemWidth);
        mDstB = makeDst(itemWidth, itemWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);
        int r = getWidth() / 3;

        //drawNormal(canvas, r);

        //clearModel(canvas, r);

        //clearModelInNewLayer(canvas, r);
        clearModelInNewLayerInBitmap(canvas);
    }

    private void clearModel(Canvas canvas, int r) {
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[0]);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
    }

    /**
     * 在单独的图层上绘制
     * <p>
     * 注意对比和 {@link #clearModel(Canvas, int)}方法的区别
     *
     * @param canvas
     * @param r
     */
    private void clearModelInNewLayer(Canvas canvas, int r) {
        //创建一个新的图层
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[0]);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //将新图层和旧的图层混合
        canvas.restore();

    }

    /**
     * 在单独的图层上绘制,并且绘制的是bitmap
     * <p>
     * 注意对比和 {@link #clearModel(Canvas, int)}方法的区别
     *
     * @param canvas
     */
    private void clearModelInNewLayerInBitmap(Canvas canvas) {
        //创建一个新的图层
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawBitmap(mDstB, 0, 0, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[4]);
        canvas.drawBitmap(mSrcB, 0, 0, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //将新图层和旧的图层混合
        canvas.restore();
    }

    private void drawNormal(Canvas canvas, int r) {
        //绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //绘制蓝色的矩形
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
    }

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
}
