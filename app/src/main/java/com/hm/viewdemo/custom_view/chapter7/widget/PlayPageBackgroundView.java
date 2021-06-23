package com.hm.viewdemo.custom_view.chapter7.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.util.ScreenUtil;

/**
 * Created by WolfXu on 2020-05-07.
 * 播放页背景控件
 *
 * @author WolfXu
 * @email wolf.xu@ximalaya.com
 * @phoneNumber 13670268092
 */
public class PlayPageBackgroundView extends View {

    private static final int MAIN_COLOR_ALPHA = (int) (255 * 0.7);
    private int mDefaultColor = 0xff444444;
    private volatile Bitmap mImage;
    private int mMainColor = mDefaultColor;
    private int mWidth;
    private int mHeight;
    private Rect mRect = new Rect();
    private Paint mGradientMaskPaint;
    private Paint mSolidMaskPaint;
    private Paint mDefaultBgPaint;

    private boolean shouldDrawAlphaLayer = true;
    private boolean shouldDrawGradientLayer = true;

    public PlayPageBackgroundView(Context context) {
        this(context, null);
    }

    public PlayPageBackgroundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayPageBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void setShouldDrawAlphaLayer(boolean shouldDrawAlphaLayer) {
        this.shouldDrawAlphaLayer = shouldDrawAlphaLayer;
    }

    public void setShouldDrawGradientLayer(boolean shouldDrawGradientLayer) {
        this.shouldDrawGradientLayer = shouldDrawGradientLayer;
    }

    private void init() {
        mMainColor = changeColorAlpha(mDefaultColor, MAIN_COLOR_ALPHA);
        mGradientMaskPaint = new Paint();
        updateGradientMaskPaint();
        mSolidMaskPaint = new Paint();
        mSolidMaskPaint.setColor(mMainColor);
        mDefaultBgPaint = new Paint();
        mDefaultBgPaint.setColor(mDefaultColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mImage == null) {
            canvas.drawRect(mRect, mDefaultBgPaint);
        } else {
            canvas.drawBitmap(mImage, null, mRect, null);
        }
        if (shouldDrawAlphaLayer) {
            canvas.drawRect(mRect, mSolidMaskPaint);
        }
        if (shouldDrawGradientLayer) {
            canvas.drawRect(mRect, mGradientMaskPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRect.bottom = mHeight;
        mRect.right = mWidth;
        updateGradientMaskPaint();
    }

    private void updateGradientMaskPaint() {
        int[] gradientColor = new int[]{0x0d000000, 0x26000000, 0x80000000};
        float[] gradientPosition = new float[]{0, 0.5f, 1};
        int gradientY1 = mHeight;
        if (gradientY1 <= 0) {
            gradientY1 = ScreenUtil.getScreenHeight(getContext());
        }
        mGradientMaskPaint.setShader(new LinearGradient(0, 0, 0, gradientY1, gradientColor, gradientPosition
                , Shader.TileMode.CLAMP));
    }

    public void setImageAndColor(Bitmap image, int color) {
        mImage = image;
        mMainColor = changeColorAlpha(color, MAIN_COLOR_ALPHA);
        mSolidMaskPaint.setColor(mMainColor);
        invalidate();
    }

    public void setDefaultColor(int defaultColor) {
        mDefaultColor = defaultColor;
    }

    private int changeColorAlpha(int srcColor, int alpha) {
        return Color.argb(alpha, Color.red(srcColor), Color.green(srcColor), Color.blue(srcColor));
    }
}
