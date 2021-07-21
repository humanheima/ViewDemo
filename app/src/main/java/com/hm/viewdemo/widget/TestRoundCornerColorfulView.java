package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;


/**
 * Created by dumingwei on 2021/7/19
 * <p>
 * Desc:测试绘制进度条，实现圆角的功能。
 */
public class TestRoundCornerColorfulView extends View {

    private static final String TAG = "TestRoundCornerColorful";

    private PorterDuffXfermode xfermode;// 图形混合模式

    private Paint paint;
    private Paint paint2;
    private int mWidth;

    private int originalHeight;

    public TestRoundCornerColorfulView(Context context, int width, Paint paint, Paint paint2) {
        super(context);
        mWidth = width;
        this.paint = paint;
        this.paint2 = paint2;

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mWidth);
        Log.i(TAG, "onMeasure: " + getMeasuredWidth() + "  " + getMeasuredHeight());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        float x, y;
        float x2, y2;
        for (int i = 20; i > 0; i--) {
            Path p1 = new Path();
            x = y = ((float) mWidth / 20) * i;
            p1.lineTo(0, y);
            p1.lineTo(x, 0);
            p1.lineTo(0, 0);
            p1.close();
            if (i % 2 == 0) {
                canvas.drawPath(p1, paint);
            } else {
                canvas.drawPath(p1, paint2);
            }
        }
        for (int i = 0; i < 20; i++) {
            Path p2 = new Path();
            x2 = y2 = ((float) mWidth / 20) * i;
            p2.moveTo(mWidth, mWidth);
            p2.lineTo(mWidth, y2);
            p2.lineTo(x2, mWidth);
            p2.lineTo(mWidth, mWidth);
            p2.close();
            if (i % 2 != 0) {
                canvas.drawPath(p2, paint);
            } else {
                canvas.drawPath(p2, paint2);
            }
        }

        paint.setXfermode(xfermode);

        int width = getMeasuredWidth();

        canvas.drawRoundRect(0f, 0f, width * 1.0f, originalHeight * 10f, 16, 16, paint);

        paint.setXfermode(null);
    }


}
