package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by dumingwei on 2017/11/30 0030.
 * Desc:
 * saveLayer可以为canvas创建一个新的透明图层，在新的图层上绘制，并不会直接绘制到屏幕上，
 * 而会在restore之后，绘制到上一个图层或者屏幕上（如果没有上一个图层）。
 * 为什么会需要一个新的图层，例如在处理xfermode的时候，原canvas上的图（包括背景）会影响src和dst的合成，
 * 这个时候，使用一个新的透明图层是一个很好的选择。又例如需要当前绘制的图形都带有一定的透明度，
 * 那么创建一个带有透明度的图层，也是一个方便的选择。
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

        int itemWidth = ScreenUtil.dpToPx(context, 200);
        mSrcB = makeSrc(itemWidth, itemWidth);
        mDstB = makeDst(itemWidth, itemWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //创建一个新的图层
        int layerId = canvas.saveLayer(0f, 0f, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);

        //目标图像（DST）和图像（SRC）混合的操作
        canvas.drawBitmap(mDstB, 0, 0, paint);
        paint.setXfermode(sModes[1]);
        canvas.drawBitmap(mSrcB, 0, 0, paint);
        paint.setXfermode(null);

        //将新的图层绘制到上一个图层或者屏幕上（如果没有上一个图层）。
        canvas.restoreToCount(layerId);

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
        //todo 这样 saveLayer是不对的，保存的图层就是一个宽高都为0的图层，所以什么也看不到。
        canvas.saveLayer(new RectF(), null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[0]);
        paint.setColor(0x66AAFF);
        canvas.drawRect(r, r, r * 2.7f, r * 2.7f, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //将新的图层绘制到上一个图层或者屏幕上（如果没有上一个图层）。
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
        //canvas.save();
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        //paint.setColor(0xFFFFCC44);
        canvas.drawBitmap(mDstB, 0, 0, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[11]);
        canvas.drawBitmap(mSrcB, 0, 0, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        //将新的图层绘制到上一个图层或者屏幕上（如果没有上一个图层）。
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

    public Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置的透明度是FF
        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w * 3 / 4f, h * 3 / 4f), p);
        return bm;
    }

    Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置的透明度是FF
        p.setColor(0xFF66AAFF);
        c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p);
        return bm;
    }
}
