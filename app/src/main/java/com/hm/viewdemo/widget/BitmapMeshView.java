package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.hm.viewdemo.R;

public class BitmapMeshView extends View {

    private static final String TAG = "BitmapMeshView";

    private Bitmap bitmap;
    private Paint paint;
    private int meshWidth = 8; // 网格宽度
    private int meshHeight = 8; // 网格高度
    private float[] verts; // 顶点数组

    public BitmapMeshView(Context context, AttributeSet attrs) {
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
                // 根据顶点位置调整 Y 坐标实现边缘向内弯曲效果
                float offset = (float) Math.sin((j / (float) meshWidth) * Math.PI) * 30; // 使得边缘向内弯曲
                verts[index + 1] = i * (bitmap.getHeight() / (float) meshHeight) - offset;
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // 绘制位图网格
        canvas.drawBitmapMesh(bitmap, meshWidth, meshHeight, verts, 0, null, 0, paint);
    }
}