package com.hm.viewdemo.widget.hongyang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/11/30 0030.
 */

public class RoundImageViewCopy extends AppCompatImageView {

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;

    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 10;

    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圆角的半径
     */
    private int mRadius;
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;
    private RectF mRoundRect;


    /**
     * 动态绘制扇形
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int swipeAngle = 360;
    private int startAngle = -90;
    private Runnable action;
    private int arcColor;

    private RectF rectF;


    public RoundImageViewCopy(Context context) {
        this(context, null);
    }

    public RoundImageViewCopy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageViewCopy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        mMatrix = new Matrix();

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        BORDER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        type = a.getInt(R.styleable.RoundImageView_imageType, TYPE_CIRCLE);
        a.recycle();


        arcColor = context.getResources().getColor(R.color.colorAccent);
        rectF = new RectF();

        mPaint.setColor(arcColor);
        action = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (type == TYPE_CIRCLE || type == TYPE_ROUND) {
            if (this.type != type) {
                this.type = type;
                requestLayout();
            }
        }
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth >> 1;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (null == drawable) {
            return;
        }
        Bitmap bitmap = drawableToBitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = mWidth * 1.0f / bSize;
        } else if (type == TYPE_ROUND) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        mBitmapPaint.setShader(mBitmapShader);

    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = ((BitmapDrawable) drawable);
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == getDrawable()) {
            return;
        }
        setUpShader();
        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
        } else if (type == TYPE_CIRCLE) {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
        }

        mPaint.setColor(arcColor);
        rectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawArc(rectF, startAngle, swipeAngle, true, mPaint);

        if (swipeAngle > 0) {
            startAngle++;
            swipeAngle--;
            //postDelayed(action, 20);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND) {
            mRoundRect = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = ((Bundle) state);
            super.onRestoreInstanceState(((Bundle) state).getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
