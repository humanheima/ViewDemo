package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2020/5/7
 * <p>
 * Desc: 绘制View渐变背景LinearGradient，从左到右水平渐变
 * 可以只传入一个颜色，则不会渐变
 */
public class LinearGradientView extends View {

    private static final String TAG = "LinearGradientView";

    private Paint paint = new Paint();
    private LinearGradient backGradient;
    private int startColor;
    private int endColor;

    private Color color;

    public LinearGradientView(Context context) {
        this(context, null);
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LinearGradientView);
        startColor = ta.getInt(R.styleable.LinearGradientView_start_color, R.color.vip_start_color);
        endColor = ta.getInt(R.styleable.LinearGradientView_end_color, R.color.vip_end_color);
        ta.recycle();
    }

    /**
     * @param color 不用渐变
     */
    public void setSameColor(int color) {
        this.startColor = color;
        this.endColor = color;
        backGradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        invalidate();
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
        backGradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        invalidate();
    }


    public void setEndColor(int endColor) {
        this.endColor = endColor;
        backGradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        invalidate();
    }

    public void setColors(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        backGradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取View的宽高
        int width = getWidth();
        int height = getHeight();
        paint.setShader(null);
        paint.setShader(backGradient);
        canvas.drawRect(0, 0, width, height, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged: ");
        super.onSizeChanged(w, h, oldw, oldh);
        //从左到右水平变化
        backGradient = new LinearGradient(0, 0, getWidth(), 0,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
    }
}