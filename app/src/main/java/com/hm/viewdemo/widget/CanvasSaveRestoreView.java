package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dumingwei on 2020/6/8
 * <p>
 * Desc:
 * 关于Canvas save  和 restore 功能
 * <p>
 * 1. 分层绘制
 * 2. 简化 操作，比如我们要绘制一个向右的箭头
 * 2.1 调用save方法新建图层
 * 2.2 将画布绕着中心点旋转90度，绘制一个竖直向上的箭头
 * 2.3 restore
 * <p>
 * 3. 在处理xfermode的时候，原canvas上的图（包括背景）会影响src和dst的合成，这个时候，使用一个新的透明图层是一个很好的选择。
 * <p>
 * 4. 创建带透明度的图层。
 */
public class CanvasSaveRestoreView extends View {

    private static final String TAG = "MyView";
    private Paint mPaint = null;

    private float px = 500f;
    private float py = 500f;

    public CanvasSaveRestoreView(Context context) {
        this(context, null);

    }

    public CanvasSaveRestoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasSaveRestoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public void init(Context context, AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, px, py, mPaint);


        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.RED);

        canvas.save();

        canvas.rotate(90, px / 2, py / 2);

        // 左边的斜杠
        canvas.drawLine(px / 2, 0, 0, py / 2, mPaint);

        // 右边的斜杠
        canvas.drawLine(px / 2, 0, px, py / 2, mPaint);

        // 垂直的竖杠
        canvas.drawLine(px / 2, 0, px / 2, py, mPaint);

        canvas.restore();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(px - 100, py - 100, 50, mPaint);
    }
}