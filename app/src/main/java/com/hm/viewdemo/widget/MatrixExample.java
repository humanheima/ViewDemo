package com.hm.viewdemo.widget;

import android.graphics.Matrix;
import android.util.Log;

/**
 * Created by p_dmweidu on 2024/11/23
 * Desc: 测试矩阵
 */
public class MatrixExample {

    private static final String TAG = "MatrixExample";

    public static void main() {
        // 创建一个 Matrix 对象
        Matrix matrix = new Matrix();

        // 应用一个平移变换
        //float translateX = 100;
        //float translateY = 50;

        float translateX = -165;
        float translateY = 165;
//        matrix.preTranslate(translateX, translateY);
//        matrix.preTranslate(165, -165);

        // 定义源点数组 (x, y) 对
//        float[] srcPoints = {
//                0, 0,   // 点 1
//                50, 50, // 点 2
//                100, 100 // 点 3
//        };

        float[] srcPoints = {
                0, 0,   // 点 1
                165,0,
                330,0,
                0,165,
                165,165,
                330,165,
                0,330,
                165,330,
                330,330,
        };

        // 定义目标点数组，用于存储转换后的结果
        float[] dstPoints = new float[srcPoints.length];

        // 使用 mapPoints 进行转换
        matrix.mapPoints(dstPoints, srcPoints);

        // 输出转换后的点
        for (int i = 0; i < dstPoints.length; i += 2) {
            Log.d(TAG, "Transformed Point: (" + dstPoints[i] + ", " + dstPoints[i + 1] + ")");
        }
    }
}