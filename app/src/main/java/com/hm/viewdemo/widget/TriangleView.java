package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by p_dmweidu on 2025/3/1
 * Desc: 使用 Path 绘制一个三角形，使用二次贝塞尔曲线绘制到顶部，创建圆滑顶角。
 */
public class TriangleView extends View {
    private Paint paint;
    private Path path;

    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE); // 设置颜色
        paint.setStyle(Paint.Style.FILL); // 填充样式
        paint.setAntiAlias(true); // 抗锯齿
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        canvas.drawColor(Color.GRAY);
        path.reset();

        // 定义关键点
        float topX = width / 2;           // 顶部顶点X
        //float topY = height * 0.2f;       // 顶部顶点Y
        float topY = 0f;       // 顶部顶点Y
        float bottomLeftX = 0; // 左下角X
        float bottomRightX = width;// 右下角X
        float bottomY = height;  // 底边Y

        /**
         * 计算控制点用于圆滑顶角
         * 可以通过调整 controlOffset 的值来控制圆角的大小，比如改成 width * 0.05f 会得到更小的圆角，改成 width * 0.15f 会得到更大的圆角。
         */
        float controlOffset = width * 0.1f; // 控制圆角的大小

        // 开始绘制
        path.moveTo(bottomLeftX, bottomY); // 从左下角开始

        // 连接到右下角
        path.lineTo(bottomRightX, bottomY);

        // 使用二次贝塞尔曲线绘制到顶部，创建圆滑顶角
        path.lineTo(topX + controlOffset, topY + controlOffset);
        //topX，topY 是控制点，topX + controlOffset，topY + controlOffset 是终点
        path.quadTo(topX, topY, topX - controlOffset, topY + controlOffset);

        // 回到左下角
        path.lineTo(bottomLeftX, bottomY);
        path.close();

        // 绘制路径
        canvas.drawPath(path, paint);
    }
}