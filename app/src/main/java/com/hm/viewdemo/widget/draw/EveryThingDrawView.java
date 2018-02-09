package com.hm.viewdemo.widget.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/11/30 0030.
 * {@link Paint.Style#FILL}
 * {@link Paint.Style#FILL_AND_STROKE}
 * 两者的区别在于 以画圆为例 如果 paint设置了setStrokeWidth ，那么FILL 模式下画出来的圆要比
 * FILL_AND_STROKE 模式下画出来的小
 */

public class EveryThingDrawView extends View {

    private Paint paint;
    private int radius;
    private int width;
    private int height;
    private RectF rectF;
    private Path path = new Path();

    private Bitmap bitmap;

    private Rect srcRect;
    private RectF dstRect;
    private Bitmap dst, src;
    private PathEffect pathEffect;

    public EveryThingDrawView(Context context) {
        this(context, null);
    }

    public EveryThingDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EveryThingDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        rectF = new RectF(100, 100, 300, 300);
        srcRect = new Rect(0, 0, 100, 100);
        dstRect = new RectF(100, 100, 200, 200);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.composite_dst);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.composite_src);
        //phase 表示 起始点的偏移量
        pathEffect = new DashPathEffect(new float[]{40, 5,80,20}, 30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) >> 1;
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width >> 1, height >> 1, radius, paint);*/
        /*paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(dp2px(20));
        paint.setColor(Color.parseColor("#FFFF3344"));
        canvas.drawCircle(width >> 1, height >> 1, radius >> 1, paint);*/
        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(dp2px(2));
        /*canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FFFF3344"));*/
        //paint.setStrokeCap(Paint.Cap.SQUARE);
        //paint.setStrokeCap(Paint.Cap.BUTT);
        //canvas.drawPoint(30, 30, paint);

        //float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
        // 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
        /* 跳过两个数，即前两个 0 */
        /* 一共绘制的点数 count>>1 */
        //canvas.drawPoints(points, 2, 12, paint);
        //canvas.drawPoints(points,paint);
        //画椭圆
        //canvas.drawOval(rectF, paint);
        //paint.setStrokeWidth(dp2px(2));
        //canvas.drawLine(10, 10, 200, 50, paint);
        //float[] points = {20, 20, 120, 20, 70, 20, 70, 120, 20, 120, 120, 120, 150, 20, 250, 20,150, 20, 150, 120, 250, 20, 250, 120, 150, 120, 250, 120};
        //canvas.drawLines(points, paint);
        //canvas.drawLines(points, 4, 24, paint);
        //canvas.drawRoundRect(rectF, dp2px(8), dp2px(8), paint);
        //paint.setStyle(Paint.Style.FILL);
        //canvas.drawArc(rectF,0,90,true,paint);

        // TODO: 2017/12/1 0001   path 没有搞明白
        /*path.lineTo(100, 100); // 画斜线
        path.moveTo(200, 0); // 我移~~
        path.lineTo(200, 100); // 画竖线
        canvas.drawPath(path, paint);*/
       /* paint.setStyle(Paint.Style.STROKE);
        path.lineTo(100, 100);
        // 强制移动到弧形起点（无痕迹）
        path.addArc(rectF, -90, 0);
        canvas.drawPath(path, paint);*/

       /* path.moveTo(100, 100);
        path.lineTo(200, 100);
        path.lineTo(150, 150);
        path.close(); // 使用 close() 封闭子图形。等价于 path.lineTo(100, 100)
        canvas.drawPath(path,paint);*/

        //canvas.drawBitmap(bitmap,0,0,null);
        //canvas.drawBitmap(bitmap, srcRect, dstRect, null);
       /* paint.setTextSize(sp2px(14));
        canvas.drawText("hello world", 0, 100, paint);*/
        //testPorterDuffMode(canvas);
        paint.setPathEffect(pathEffect);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        //canvas.drawCircle(300, 300, 200, paint);
        canvas.drawLine(40,40,400,400,paint);
    }

    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public void testPorterDuffMode(Canvas canvas) {
        int count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dst, 0, 0, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(src, 0, 0, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(count);
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public int sp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}
