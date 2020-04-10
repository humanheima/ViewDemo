package com.hm.viewdemo.widget.hongyang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/11/30 0030.
 */

public class RoundImageViewByXfermodeCopy extends AppCompatImageView {

    private Paint mPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Bitmap mBitmap;
    private RectF mRectF;

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    public RoundImageViewByXfermodeCopy(Context context) {
        this(context, null);
    }

    public RoundImageViewByXfermodeCopy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageViewByXfermodeCopy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageViewByXfermode);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BORDER_RADIUS_DEFAULT,
                        getResources().getDisplayMetrics()));
        type = a.getInt(R.styleable.RoundImageViewByXfermode_imageType, TYPE_CIRCLE);
        mRectF = new RectF();
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
        Drawable drawable = getDrawable();
        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();
        if (null != drawable) {
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas drawCanvas = new Canvas(mBitmap);
                float scale;
                if (type == TYPE_ROUND) {
                    scale = Math.max(getWidth() * 1.0f / dWidth, getHeight() * 1.0f / dHeight);
                } else {
                    scale = getWidth() * 1.0f / Math.min(dWidth, dHeight);
                }
                //根据缩放比例，设置bounds，相当于缩放图片了
                drawable.setBounds(0, 0, (int) (scale * dWidth), (int) (scale * dHeight));
                drawable.draw(drawCanvas);
            }

            mRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());

            mPaint.setXfermode(null);

            if (type == TYPE_ROUND) {
                canvas.drawRoundRect(mRectF,
                        mBorderRadius, mBorderRadius, mPaint);
            } else {
                float cx = getWidth() / 2f;
                canvas.drawCircle(cx, cx, cx, mPaint);
            }
            mPaint.setXfermode(mXfermode);
            canvas.drawBitmap(mBitmap, 0, 0, mPaint);
            //将save保存的图层叠加到上一个图层上
            canvas.restore();
        }
    }
}
