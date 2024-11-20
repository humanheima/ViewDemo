package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomVertexView extends View {
    private Paint paint;
    private float[] vertices;
    private int[] colors;

    public CustomVertexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // 定义顶点坐标
        vertices = new float[]{
                100, 100,   // 1. 顶点1
                200, 50,    // 2. 顶点2
                300, 100,   // 3. 顶点3
                350, 200,   // 4. 顶点4
                200, 300,   // 5. 顶点5
                100, 200    // 6. 顶点6
        };

        // 定义颜色数组
        colors = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.YELLOW,
                Color.CYAN,
                Color.MAGENTA
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置颜色
        paint.setColor(Color.BLACK);

        // 使用 drawVertices 方法绘制多边形
        canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN,
                vertices.length, vertices, 0, null, 0, colors, 0, null, 0, 0, paint);
    }
}