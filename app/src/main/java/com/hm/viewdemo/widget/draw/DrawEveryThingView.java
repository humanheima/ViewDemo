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

public class DrawEveryThingView extends View {

    private Paint mPaint;
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

    public DrawEveryThingView(Context context) {
        this(context, null);
    }

    public DrawEveryThingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawEveryThingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        rectF = new RectF(100, 100, 300, 300);
        srcRect = new Rect(0, 0, 100, 100);
        dstRect = new RectF(100, 100, 200, 200);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aa);
        dst = BitmapFactory.decodeResource(getResources(), R.drawable.composite_dst);
        src = BitmapFactory.decodeResource(getResources(), R.drawable.composite_src);
        //phase 表示 起始点的偏移量
        pathEffect = new DashPathEffect(new float[]{40, 5, 80, 20}, 30);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);           // 画笔颜色 - 黑色
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(sp2px(14));
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
        //绘制坐标系
        //mPaint.setColor(Color.RED);
        //canvas.drawLine(0f, height / 2.0f, width * 1.0f, height / 2.0f, mPaint);
        //canvas.drawLine(width / 2.0f, 0f, width / 2.0f, height * 1.0f, mPaint);
        /*mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width >> 1, height >> 1, radius, mPaint);*/
        /*mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(dp2px(20));
        mPaint.setColor(Color.parseColor("#FFFF3344"));
        canvas.drawCircle(width >> 1, height >> 1, radius >> 1, mPaint);*/
        //mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStrokeWidth(dp2px(2));
        /*canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#FFFF3344"));*/
        //mPaint.setStrokeCap(Paint.Cap.SQUARE);
        //mPaint.setStrokeCap(Paint.Cap.BUTT);
        //canvas.drawPoint(30, 30, mPaint);

        //float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
        // 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
        /* 跳过两个数，即前两个 0 */
        /* 一共绘制的点数 count>>1 */
        //canvas.drawPoints(points, 2, 12, mPaint);
        //canvas.drawPoints(points,mPaint);
        //画椭圆
        //canvas.drawOval(rectF, mPaint);
        //mPaint.setStrokeWidth(dp2px(2));
        //canvas.drawLine(10, 10, 200, 50, mPaint);
        //float[] points = {20, 20, 120, 20, 70, 20, 70, 120, 20, 120, 120, 120, 150, 20, 250, 20,150, 20, 150, 120, 250, 20, 250, 120, 150, 120, 250, 120};
        //canvas.drawLines(points, mPaint);
        //canvas.drawLines(points, 4, 24, mPaint);
        //canvas.drawRoundRect(rectF, dp2px(8), dp2px(8), mPaint);
        //mPaint.setStyle(Paint.Style.FILL);
        //canvas.drawArc(rectF,0,90,true,mPaint);

        // TODO: 2017/12/1 0001   path 没有搞明白

       /* mPaint.setStyle(Paint.Style.STROKE);
        path.lineTo(100, 100);
        // 强制移动到弧形起点（无痕迹）
        path.addArc(rectF, -90, 0);
        canvas.drawPath(path, mPaint);*/

        //testPorterDuffMode(canvas);

        /*mPaint.setPathEffect(pathEffect);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        //canvas.drawCircle(300, 300, 200, mPaint);
        canvas.drawLine(40, 40, 400, 400, mPaint);*/

        //testPathLineTo(canvas);
        //testPathAddRect(canvas);
        //testPathAddPath(canvas);
        //testPathAddArc(canvas);
        //testPathArcTo(canvas);
        //pathFillTypeEVenOld(canvas);
        //pathFillTypeWinding(canvas);
        //pathOp(canvas);
        pathComputeBound(canvas);
    }

    private void pathComputeBound(Canvas canvas) {
        canvas.translate(width / 2f, height / 2f);

        RectF rect1 = new RectF();              // 存放测量结果的矩形

        Path path = new Path();                 // 创建Path并添加一些内容
        path.lineTo(100, -50);
        path.lineTo(100, 50);
        path.close();
        path.addCircle(-100, 0, 100, Path.Direction.CW);

        path.computeBounds(rect1, true);         // 测量Path

        canvas.drawPath(path, mPaint);    // 绘制Path

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect1, mPaint);   // 绘制边界
    }


    /**
     * Path boolean 操作
     *
     * @param canvas
     */
    private void pathOp(Canvas canvas) {
        int x = 80;
        int r = 100;

        canvas.translate(250, 0);

        Path path1 = new Path();
        Path path2 = new Path();
        Path pathOpResult = new Path();

        path1.addCircle(-x, 0, r, Path.Direction.CW);
        path2.addCircle(x, 0, r, Path.Direction.CW);

        pathOpResult.op(path1, path2, Path.Op.DIFFERENCE);

        canvas.translate(0, 200);
        canvas.drawText("DIFFERENCE", 240, 0, mPaint);
        canvas.drawPath(pathOpResult, mPaint);

        pathOpResult.op(path1, path2, Path.Op.REVERSE_DIFFERENCE);
        canvas.translate(0, 300);
        canvas.drawText("REVERSE_DIFFERENCE", 240, 0, mPaint);
        canvas.drawPath(pathOpResult, mPaint);

        pathOpResult.op(path1, path2, Path.Op.INTERSECT);
        canvas.translate(0, 300);
        canvas.drawText("INTERSECT", 240, 0, mPaint);
        canvas.drawPath(pathOpResult, mPaint);

        pathOpResult.op(path1, path2, Path.Op.UNION);
        canvas.translate(0, 300);
        canvas.drawText("UNION", 240, 0, mPaint);
        canvas.drawPath(pathOpResult, mPaint);

        pathOpResult.op(path1, path2, Path.Op.XOR);
        canvas.translate(0, 300);
        canvas.drawText("XOR", 240, 0, mPaint);
        canvas.drawPath(pathOpResult, mPaint);

    }


    /**
     * 测试 path 填充模式 奇偶规则
     * 测试发现 EVEN_ODD 和 INVERSE_EVEN_ODD 效果是一样的
     *
     * @param canvas
     */
    private void pathFillTypeEVenOld(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        canvas.translate(width / 2.0f, height / 2.0f);
        mPaint.setColor(Color.BLACK);

        Path path = new Path();


        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        //path.setFillType(Path.FillType.EVEN_ODD);                   // 设置Path填充模式为 奇偶规则
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);           // 反奇偶规则


        canvas.drawPath(path, mPaint);

    }

    /**
     * 测试 path 填充模式 非零环绕数规则
     *
     * @param canvas
     */
    private void pathFillTypeWinding(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        canvas.translate(width / 2.0f, height / 2.0f);
        mPaint.setColor(Color.BLACK);

        Path path = new Path();

        // 添加小正方形 (通过这两行代码来控制小正方形边的方向,从而演示不同的效果)
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);
        //path.addRect(-200, -200, 200, 200, Path.Direction.CCW);


        // 添加大正方形
        path.addRect(-400, -400, 400, 400, Path.Direction.CCW);

        path.setFillType(Path.FillType.WINDING);


        canvas.drawPath(path, mPaint);

    }

    /**
     * @param canvas
     */
    private void testPathLineTo(Canvas canvas) {
        //移动到屏幕中间
        canvas.translate(width / 2.0f, height / 2.0f);
        mPaint.setColor(Color.BLACK);
        Path path = new Path();
        path.lineTo(200, 200);
        //path.setLastPoint(200,100);//该方法会影响之前和之后的操作，去掉这行代码对比一下，就看出来了
        path.lineTo(200, 0);
        path.close();

        canvas.drawPath(path, mPaint);
    }


    /**
     * 测试 path 添加矩形
     *
     * @param canvas
     */
    private void testPathAddRect(Canvas canvas) {
        //移动到屏幕中间
        mPaint.setColor(Color.BLACK);
        canvas.translate(width / 2.0f, height / 2.0f);
        Path path = new Path();
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);
        canvas.drawPath(path, mPaint);
    }


    /**
     * 测试 path.arcTo
     *
     * @param canvas
     */
    private void testPathArcTo(Canvas canvas) {
        //移动到屏幕中间
        mPaint.setColor(Color.BLACK);
        canvas.translate(width / 2.0f, height / 2.0f);

        Path path = new Path();
        path.lineTo(100, 100);

        RectF oval = new RectF(0, 0, 300, 300);
        path.arcTo(oval, 0, 270);

        canvas.drawPath(path, mPaint);
    }

    /**
     * 测试 path 添加弧形
     *
     * @param canvas
     */
    private void testPathAddArc(Canvas canvas) {
        //移动到屏幕中间
        mPaint.setColor(Color.BLACK);
        canvas.translate(width / 2.0f, height / 2.0f);

        Path path = new Path();
        path.lineTo(100, 100);

        RectF oval = new RectF(0, 0, 300, 300);
        path.addArc(oval, 0, 270);

        canvas.drawPath(path, mPaint);
    }

    /**
     * 合并path
     *
     * @param canvas
     */
    private void testPathAddPath(Canvas canvas) {
        //移动到屏幕中间
        mPaint.setColor(Color.BLACK);
        canvas.translate(width / 2.0f, height / 2.0f);
        Path path = new Path();
        Path src = new Path();
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);
        src.addCircle(0, 0, 100, Path.Direction.CW);

        path.addPath(src, 0, 200);

        canvas.drawPath(path, mPaint);
    }

    private Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public void testPorterDuffMode(Canvas canvas) {
        int count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(dst, 0, 0, mPaint);
        mPaint.setXfermode(xfermode);
        canvas.drawBitmap(src, 0, 0, mPaint);
        mPaint.setXfermode(null);
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
