package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hm.viewdemo.R;

/**
 * Description:</br>
 *
 * @author:muye@in66.com </br>
 * Date:2017/12/29 上午10:31
 */

public class CurveImageView2 extends androidx.appcompat.widget.AppCompatImageView {

    private static final String TAG = "CurveImageView";

    public static final int NEED_PADDING = 20;
    private Bitmap mBitmap;
    private Camera camera;

    private static final int MESH_WIDTH = 200;
    private static final int MESH_HEIGHT = 200;
    private int MESH_COUNT = (MESH_WIDTH + 1) * (MESH_HEIGHT + 1);
    private float[] dst = new float[MESH_COUNT * 2];
    private float[] origin = new float[MESH_COUNT * 2];
    private Matrix m3DMatrix;
    private float mAngle = 120;//整张图片卷起来占的角度大小
    private float mRotateY;
    private double mRadius;
    private float offsetY;

    public CurveImageView2(Context context) {
        super(context);
    }

    public CurveImageView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        camera = new Camera();
        m3DMatrix = new Matrix();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_dog_square);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, w - NEED_PADDING, h - NEED_PADDING, true);
    }

    public float getAngle() {
        return mAngle;
    }

    /***
     *
     * @param angle Bitmap宽度所跨过的角度大小
     */
    public void setAngle(float angle) {
        this.mAngle = angle;
        invalidate();
    }

    public float getRotateY() {
        return mRotateY;
    }

    /***
     *
     * @param rotate 绕柱中心的旋转角度
     */
    public void setRotateY(float rotate) {
        this.mRotateY = rotate;
        invalidate();
    }

    public void initMesh() {
        mRadius = mBitmap.getWidth() / (2 * Math.sin(mAngle / 2 * Math.PI / 180));

        int index = 0;
        float bw = mBitmap.getWidth();
        float bh = mBitmap.getHeight();
        for (int i = 0; i < MESH_HEIGHT + 1; i++) {
            float fy = bh / MESH_HEIGHT * i;
            for (int j = 0; j < MESH_WIDTH + 1; j++) {
                float fx = bw / MESH_WIDTH * j;


                origin[index * 2 + 0] = fx;
                origin[index * 2 + 1] = fy;
                float d = Math.abs(fx - bw / 2);
                double offsetZ = mRadius * (1 - Math.cos(Math.asin(d / mRadius)));
                //Log.d(TAG, "initMesh: bw :" + bw + " bh:" + bh + " fx :" + fx + " fy:" + fy + " offsetZ = "+offsetZ);
                Log.d(TAG, " fx :" + fx + " fy:" + fy + " offsetZ = " + offsetZ);
                camera.save();
                camera.translate(0, 0, (float) offsetZ);
                camera.getMatrix(m3DMatrix);
                camera.restore();
                float[] point = null;
                float px = fx;
                float py = fy;

                //Log.d(TAG, "initMesh: width :" + bw + " height:" + bh + " fx:" + fx + " fy:" + fy);

                //变化坐标是[0，0]
                //当前点的坐标
                point = new float[]{px, py};
                //dx > 0 ，向右移动，dx < 0 会向左移动
                //dy > 0 会向下移动,dy < 0 会向上移动

                /**
                 * 这种移动方式，是让每一个点都以中心点进行变换。这种方式是对的。
                 */
//                m3DMatrix.preTranslate(-bw / 2, -bh / 2);
//                m3DMatrix.postTranslate(bw / 2, bh / 2);
//                m3DMatrix.mapPoints(point);


                /**
                 * 这种方式，是让每一个点都以水平中心点，竖直方向上以[水平中心，当前坐标+ bh / 2 ]点进行变换。视觉上有更好弯曲效果
                 */
                m3DMatrix.preTranslate(-bw / 2, bh / 2);
                m3DMatrix.postTranslate(bw / 2, -bh / 2);
                m3DMatrix.mapPoints(point);

                dst[index * 2 + 0] = point[0];
                dst[index * 2 + 1] = point[1];

                //Log.e(TAG, "initMesh: px:" + point[0] + " py:" + point[1]);

                index++;
            }
            Log.e(TAG, "initMesh: 换行");
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        initMesh();
        canvas.save();
        canvas.translate(NEED_PADDING / 2, NEED_PADDING / 2);
        canvas.drawBitmapMesh(mBitmap, MESH_WIDTH, MESH_HEIGHT, dst, 0, null, 0, null);
        canvas.restore();
    }
}
