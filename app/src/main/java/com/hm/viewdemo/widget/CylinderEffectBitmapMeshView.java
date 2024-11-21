package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.R;

public class CylinderEffectBitmapMeshView extends View {
    private Bitmap bitmap;
    private Paint paint;
    private int meshWidth = 8;  // 网格宽度
    private int meshHeight = 8; // 网格高度
    private float[] verts; // 顶点数组

    public CylinderEffectBitmapMeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 加载位图
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dog);

        // 初始化 Paint
        paint = new Paint();

        // 创建顶点数组，长度为两倍
        verts = new float[(meshWidth + 1) * (meshHeight + 1) * 2];

        // 填充顶点数组
        for (int i = 0; i <= meshHeight; i++) {
            for (int j = 0; j <= meshWidth; j++) {
                int index = (i * (meshWidth + 1) + j) * 2; // 计算当前顶点的索引

                // 计算 X 坐标
                verts[index] = j * (bitmap.getWidth() / (float) meshWidth);

                // 计算 Y 坐标
                // 使得两侧的边缘向内弯曲，形成圆柱效果
                float scale = (float) Math.sin((j / (float) meshWidth) * Math.PI); // 计算弯曲比例
                verts[index + 1] = i * (bitmap.getHeight() / (float) meshHeight) - scale * 30; // 30 控制弯曲深度
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制位图网格
        canvas.drawBitmapMesh(bitmap, meshWidth, meshHeight, verts, 0, null, 0, paint);
    }
}