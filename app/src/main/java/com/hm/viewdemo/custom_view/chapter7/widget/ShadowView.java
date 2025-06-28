
package com.hm.viewdemo.custom_view.chapter7.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ShadowView extends View {
    private Paint textPaint;
    private Paint rectPaint;

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(60);
        textPaint.setColor(Color.WHITE);
        textPaint.setShadowLayer(8f, 3f, 3f, Color.BLACK); // 文本阴影

        rectPaint = new Paint();
        rectPaint.setColor(Color.RED);
        rectPaint.setShadowLayer(12f, 5f, 5f, Color.GRAY); // 矩形阴影
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Shadow Text", 50, 100, textPaint);
        canvas.drawRect(50, 150, 250, 300, rectPaint);
    }
}