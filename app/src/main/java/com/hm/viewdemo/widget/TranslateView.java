package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TranslateView extends View {
    private Paint paint;

    public TranslateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 保存当前 Canvas 状态
        canvas.save();

        // 平移 Canvas，向
        canvas.translate(100, 100); // 将绘制的内容平移到 (100, 100)

        // 绘制矩形
        canvas.drawRect(0, 0, 200, 200, paint); // 矩形原点现在在 (100, 100)

        // 恢复 Canvas 状态
        canvas.restore();

        // 在平移后绘制另一个矩形
        paint.setColor(Color.RED);
        canvas.drawRect(50, 50, 250, 250, paint); // 这个矩形未被平移
    }
}