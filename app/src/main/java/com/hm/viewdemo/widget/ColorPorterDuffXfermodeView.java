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

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by dumingwei on 2017/11/30 0030.
 * Desc:
 * saveLayer可以为canvas创建一个新的透明图层，在新的图层上绘制，并不会直接绘制到屏幕上，
 * 而会在restore之后，绘制到上一个图层或者屏幕上（如果没有上一个图层）。
 * 为什么会需要一个新的图层，例如在处理xfermode的时候，原canvas上的图（包括背景）会影响src和dst的合成，
 * 这个时候，使用一个新的透明图层是一个很好的选择。又例如需要当前绘制的图形都带有一定的透明度，
 * 那么创建一个带有透明度的图层，也是一个方便的选择。
 * <p>
 * 测试，xfermode不一定要使用bitmap作为图层载体。直接绘制Color也是可以的。
 */

public class ColorPorterDuffXfermodeView extends AppCompatImageView {

    private Paint paint;

    private Bitmap mSrcB;
    private Bitmap mDstB;

    private RectF mDstRectF = new RectF();
    private RectF mSrcRectF = new RectF();

    private int itemWidth;
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

    public ColorPorterDuffXfermodeView(Context context) {
        this(context, null);
    }

    public ColorPorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        //创建一个新的图层
        int layerId = canvas.saveLayer(0f, 0f, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
        //绘制目标图像
        paint.setColor(getResources().getColor(R.color.main_color_ffffcc44));
        mDstRectF.set(0, 0, itemWidth * 3 / 4f, itemWidth * 3 / 4f);
        canvas.drawOval(mDstRectF, paint);

        paint.setXfermode(sModes[6]);

        paint.setColor(getResources().getColor(R.color.main_color_ff66aaff));
        mSrcRectF.set(itemWidth / 3f, itemWidth / 3f, itemWidth * 19 / 20f, itemWidth * 19 / 20f);
        //mSrcRectF.set(0, 0, itemWidth * 3 / 4f, itemWidth * 3 / 4f);

        canvas.drawRect(mSrcRectF, paint);

        //canvas.drawBitmap(mSrcB, 0, 0, paint);
        paint.setXfermode(null);

        //将新的图层绘制到上一个图层或者屏幕上（如果没有上一个图层）。
        canvas.restoreToCount(layerId);

        //clearModel(canvas, 100);
        //clearModelInNewLayer(canvas, 100);

    }

    private void clearModel(Canvas canvas, int r) {
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[0]);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2f, r * 2f, paint);
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
    }

    /**
     * 在单独的图层上绘制，这个为什么是空白呢？
     * <p>
     * 注意对比和 {@link #clearModel(Canvas, int)}方法的区别
     *
     * @param canvas
     * @param r
     */
    private void clearModelInNewLayer(Canvas canvas, int r) {
        //创建一个新的图层
        canvas.saveLayer(0f, 0f, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        paint.setColor(0xFFFFCC44);
        canvas.drawCircle(r, r, r, paint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        paint.setXfermode(sModes[0]);
        paint.setColor(0xFF66AAFF);
        canvas.drawRect(r, r, r * 2f, r * 2f, paint);
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
        p.setColor(getResources().getColor(R.color.main_color_ffffcc44));
        c.drawOval(new RectF(0, 0, w * 3 / 4f, h * 3 / 4f), p);
        return bm;
    }

    Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置的透明度是FF
        p.setColor(getResources().getColor(R.color.main_color_ff66aaff));
        c.drawRect(w / 3f, h / 3f, w * 19 / 20f, h * 19 / 20f, p);
        return bm;
    }

}
