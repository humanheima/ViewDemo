package com.hm.viewdemo.widget.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Crete by dumingwei on 2019-08-13
 * Desc: 学习 path 相关知识
 */

public class DrawPathView extends View {

    private static final String TAG = "DrawPathView";

    private Paint mPaint;
    private int width;
    private int height;

    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos = new float[2];                // 当前点的实际位置
    private float[] tan = new float[2];                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作

    public DrawPathView(Context context) {
        this(context, null);
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
        mMatrix = new Matrix();

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
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制坐标系
        mPaint.setColor(Color.RED);
        canvas.drawLine(0f, height / 2.0f, width * 1.0f, height / 2.0f, mPaint);
        canvas.drawLine(width / 2.0f, 0f, width / 2.0f, height * 1.0f, mPaint);

        /*mPaint.setPathEffect(pathEffect);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        //canvas.drawCircle(300, 300, 200, mPaint);
        canvas.drawLine(40, 40, 400, 400, mPaint);*/

        //testPathLineTo(canvas);
        //testPathAddRect(canvas);
       //pathAddRoundRect(canvas);
        //testPathAddPath(canvas);
        pathAddArc(canvas);
        //testPathArcTo(canvas);
        //pathFillTypeEVenOld(canvas);
        //pathFillTypeWinding(canvas);
        //pathOp(canvas);
        //pathComputeBound(canvas);

        //getPathSegment(canvas);

        //getPathNextContour(canvas);
        //getPathPosStan(canvas);
        //getPathPosMatrix(canvas);

    }


    private void getPathPosMatrix(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(1));
        canvas.translate(width / 2f, height / 2f);

        Path path = new Path();

        path.addCircle(0, 0, 200, Path.Direction.CW);           // 添加一个圆形

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure

        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }

        // 获取当前位置的坐标以及趋势的矩阵
        measure.getMatrix(measure.getLength() * currentValue, mMatrix,
                PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);

        mMatrix.preTranslate(-mBitmap.getWidth() / 2f, -mBitmap.getHeight() / 2f);   // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)

        canvas.drawPath(path, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头

        invalidate();                                                           // 重绘页面
    }

    private void getPathPosStan(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(1));
        canvas.translate(width / 2f, height / 2f);

        Path path = new Path();

        path.addCircle(0, 0, 200, Path.Direction.CW);           // 添加一个圆形

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure

        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }

        measure.getPosTan(measure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势

        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度

        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2f, mBitmap.getHeight() / 2f);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2f, pos[1] - mBitmap.getHeight() / 2f);   // 将图片绘制中心调整到与当前点重合

        canvas.drawPath(path, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头

        invalidate();                                                           // 重绘页面
    }

    private void getPathNextContour(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(1));
        canvas.translate(width / 2f, height / 2f);

        Path path = new Path();

        path.addRect(-100, -100, 100, 100, Path.Direction.CW);  // 添加小矩形
        path.addRect(-200, -200, 200, 200, Path.Direction.CCW);  // 添加大矩形

        canvas.drawPath(path, mPaint);                    // 绘制 Path

        PathMeasure measure = new PathMeasure(path, false);     // 将Path与PathMeasure关联

        float len1 = measure.getLength();                       // 获得第一条路径的长度

        measure.nextContour();                                  // 跳转到下一条路径

        float len2 = measure.getLength();                       // 获得第二条路径的长度

        Log.i(TAG, "len1=" + len1);                              // 输出两条路径的长度
        Log.i(TAG, "len2=" + len2);

    }

    private void getPathSegment(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(1));
        canvas.translate(width / 2f, height / 2f);
        Path path = new Path();
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();

        dst.lineTo(-300, -300);

        PathMeasure measure = new PathMeasure(path, false);

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        measure.getSegment(200, 600, dst, true);
        canvas.drawPath(dst, mPaint);

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
     * 测试发现 EVEN_ODD 和 INVERSE_EVEN_ODD 效果是一样的,出现这个原因是因为没有调用Path.close方法
     *
     * @param canvas
     */
    private void pathFillTypeEVenOld(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        canvas.translate(width / 2.0f, height / 2.0f);
        mPaint.setColor(Color.BLACK);

        Path path = new Path();


        path.addRect(-200, -200, 200, 200, Path.Direction.CW);
        path.close();

        path.setFillType(Path.FillType.EVEN_ODD);                   // 设置Path填充模式为 奇偶规则
        //path.setFillType(Path.FillType.INVERSE_EVEN_ODD);           // 反奇偶规则


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
     * 测试 path 添加圆角矩形
     *
     * @param canvas
     */
    private void pathAddRoundRect(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        //移动到屏幕中间
        canvas.translate(width / 2.0f, height / 2.0f);
        Path path = new Path();
        RectF rectF1 = new RectF(50, 50, 240, 200);
        path.addRoundRect(rectF1, 10, 15, Path.Direction.CW);

        RectF rectF2 = new RectF(290, 50, 480, 200);
        float[] raddi = {10, 15, 20, 25, 30, 35, 40, 45};
        path.addRoundRect(rectF2, raddi, Path.Direction.CW);

        canvas.drawPath(path, mPaint);
    }


    /**
     * 测试 path.arcTo
     *
     * @param canvas
     */
    private void testPathArcTo(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        //移动到屏幕中间
        canvas.translate(width / 2.0f, height / 2.0f);

        Path path = new Path();
        path.moveTo(10, 10);

        RectF oval = new RectF(100, 10, 200, 100);
        //对比一下下面两行代码的区别
        //path.arcTo(oval, 0, 90);
        path.arcTo(oval, 0, 90, true);

        canvas.drawPath(path, mPaint);
    }

    /**
     * 测试 path 添加弧形
     *
     * @param canvas
     */
    private void pathAddArc(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        //移动到屏幕中间
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

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public int sp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}