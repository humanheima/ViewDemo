package com.hm.viewdemo.widget.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2017/11/30 0030.
 * [Paint.Style.FILL]
 * [Paint.Style.FILL_AND_STROKE]
 * 两者的区别在于 以画圆为例 如果 paint设置了setStrokeWidth ，那么FILL 模式下画出来的圆要比
 * FILL_AND_STROKE 模式下画出来的小
 *
 * Canvas 的 save 和 restore 对bitmap 有什么影响
 */

class DrawEveryThingView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var mPaint: Paint
    private var radius: Int = 0
    private val rectF: RectF
    private val path = Path()

    private val bitmap: Bitmap

    private val srcRect: Rect
    private val dstRect: RectF
    private val dst: Bitmap
    private val src: Bitmap
    private val pathEffect: PathEffect

    private var centerX: Int = 0
    private var centerY: Int = 0

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    init {
        initPaint()
        rectF = RectF(100f, 100f, 300f, 300f)
        srcRect = Rect(0, 0, 100, 100)
        dstRect = RectF(100f, 100f, 200f, 200f)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_soft_avatar)
        dst = BitmapFactory.decodeResource(resources, R.drawable.composite_dst)
        src = BitmapFactory.decodeResource(resources, R.drawable.composite_src)
        //phase 表示 起始点的偏移量
        pathEffect = DashPathEffect(floatArrayOf(40f, 5f, 80f, 20f), 30f)
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint.color = Color.BLACK           // 画笔颜色 - 黑色
        mPaint.style = Paint.Style.STROKE
        mPaint.textSize = sp2px(14).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radius = Math.min(measuredWidth, measuredHeight) shr 1

        centerX = measuredWidth / 2
        centerY = measuredHeight / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        testDrawOval(canvas)
        //canvas.drawColor(Color.parseColor("#FF009688"))
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

//        mPaint.style = Paint.Style.FILL
//        canvas.drawRoundRect(rectF, dp2px(8), dp2px(8), mPaint)
//
//        mPaint.style = Paint.Style.STROKE
//        mPaint.color = Color.RED
//        mPaint.strokeWidth = sp2px(2)
//        canvas.drawRoundRect(rectF, dp2px(8), dp2px(8), mPaint)

        //mPaint.setStyle(Paint.Style.FILL);
        //canvas.drawArc(rectF,0,90,true,mPaint);

        //testPorterDuffMode(canvas);

        //testTextAlign(canvas)

        //testCanvasSaveRestore1(canvas)

        //testCanvasSaveRestore2(canvas)

        //testPathArcTo(canvas);
        //pathFillTypeEVenOld(canvas)

        //testDrawLine(canvas)

    }


    private fun testDrawOval(canvas: Canvas) {

        val halfWidth = measuredWidth / 2f
        val halfHeight = measuredHeight / 2f

        //画一个横坐标
        canvas.drawLine(0f, halfHeight, measuredWidth.toFloat(), halfHeight, mPaint)

        //画一个纵坐标
        canvas.drawLine(halfWidth, 0f, halfWidth + 1, measuredHeight.toFloat(), mPaint)
        val radius = 150

        val oval = RectF(halfWidth - radius, halfHeight - radius, halfWidth + radius, halfHeight + radius)

        val path = Path()
        path.addArc(oval, 107f, -20f)

        canvas.drawPath(path, mPaint)

    }

    private fun testDrawLine(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 40f
        mPaint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(100f, 100f, 800f, 100f, mPaint)
        canvas.drawLine(800f, 100f, 800f, 800f, mPaint)
        canvas.drawLine(800f, 800f, 100f, 800f, mPaint)
    }

    private fun testCanvasSaveRestore1(canvas: Canvas) {
        canvas.drawColor(Color.RED)
        //保存当前画布大小，即整屏
        canvas.save()

        canvas.clipRect(Rect(100, 100, 800, 800))

        canvas.drawColor(Color.GREEN)

        //恢复整屏画布
        canvas.restore()

        canvas.drawColor(Color.BLUE)


    }

    private fun testCanvasSaveRestore2(canvas: Canvas) {
        canvas.drawColor(Color.RED)
        //保存当前画布大小，即整屏
        val c1 = canvas.save()

        canvas.clipRect(Rect(100, 100, 800, 800))
        canvas.drawColor(Color.GREEN)
        //保存画布大小为 Rect(100, 100, 800, 800)
        val c2 = canvas.save()

        canvas.clipRect(Rect(200, 200, 700, 700))
        canvas.drawColor(Color.BLUE)
        //保存画布大小为 Rect(200, 200, 700, 700)
        val c3 = canvas.save()

        canvas.clipRect(Rect(300, 300, 600, 600))
        canvas.drawColor(Color.BLACK)
        //保存画布大小为 Rect(300, 300, 600, 600)
        val c4 = canvas.save()

        canvas.clipRect(Rect(400, 400, 500, 500))
        canvas.drawColor(Color.WHITE)

        //将栈顶的画布状态取出来，作为当前画布，并填充为黄色
        //canvas.restoreToCount(c2)

        //canvas.drawColor(Color.YELLOW)
    }


    private fun testTextAlign(canvas: Canvas) {
        val paint = Paint()
        paint.strokeWidth = 3f
        paint.color = Color.BLACK
        //画出水平中间的分割线
        canvas.drawLine(centerX * 1.0f, 0f, centerX * 1.0f,
                measuredHeight * 1.0f, paint)

        paint.color = Color.RED
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.textSize = 40f
        //paint.textAlign = Paint.Align.LEFT
        //paint.textAlign = Paint.Align.RIGHT
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        paint.isUnderlineText = true//下划线
        paint.isStrikeThruText = true//中划线

        canvas.drawText("床前明月光", centerX * 1.0f, centerY * 1.0f, paint)
    }

    private fun pathComputeBound(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f)

        val rect1 = RectF()              // 存放测量结果的矩形

        val path = Path()                 // 创建Path并添加一些内容
        path.lineTo(100f, -50f)
        path.lineTo(100f, 50f)
        path.close()
        path.addCircle(-100f, 0f, 100f, Path.Direction.CW)

        path.computeBounds(rect1, true)         // 测量Path

        canvas.drawPath(path, mPaint!!)    // 绘制Path

        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.RED
        canvas.drawRect(rect1, mPaint!!)   // 绘制边界
    }


    /**
     * Path boolean 操作
     *
     * @param canvas
     */
    private fun pathOp(canvas: Canvas) {
        val x = 80
        val r = 100

        canvas.translate(250f, 0f)

        val path1 = Path()
        val path2 = Path()
        val pathOpResult = Path()

        path1.addCircle((-x).toFloat(), 0f, r.toFloat(), Path.Direction.CW)
        path2.addCircle(x.toFloat(), 0f, r.toFloat(), Path.Direction.CW)

        pathOpResult.op(path1, path2, Path.Op.DIFFERENCE)

        canvas.translate(0f, 200f)
        canvas.drawText("DIFFERENCE", 240f, 0f, mPaint!!)
        canvas.drawPath(pathOpResult, mPaint!!)

        pathOpResult.op(path1, path2, Path.Op.REVERSE_DIFFERENCE)
        canvas.translate(0f, 300f)
        canvas.drawText("REVERSE_DIFFERENCE", 240f, 0f, mPaint!!)
        canvas.drawPath(pathOpResult, mPaint!!)

        pathOpResult.op(path1, path2, Path.Op.INTERSECT)
        canvas.translate(0f, 300f)
        canvas.drawText("INTERSECT", 240f, 0f, mPaint!!)
        canvas.drawPath(pathOpResult, mPaint!!)

        pathOpResult.op(path1, path2, Path.Op.UNION)
        canvas.translate(0f, 300f)
        canvas.drawText("UNION", 240f, 0f, mPaint!!)
        canvas.drawPath(pathOpResult, mPaint!!)

        pathOpResult.op(path1, path2, Path.Op.XOR)
        canvas.translate(0f, 300f)
        canvas.drawText("XOR", 240f, 0f, mPaint!!)
        canvas.drawPath(pathOpResult, mPaint!!)

    }


    /**
     * 测试 path 填充模式 奇偶规则
     * 测试发现 EVEN_ODD 和 INVERSE_EVEN_ODD 效果是一样的,出现这个原因是因为没有调用Path.close方法
     *
     * @param canvas
     */
    private fun pathFillTypeEVenOld(canvas: Canvas) {

        mPaint!!.style = Paint.Style.FILL
        canvas.translate(width / 2.0f, height / 2.0f)
        mPaint!!.color = Color.BLACK

        val path = Path()


        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        path.close()

        path.fillType = Path.FillType.EVEN_ODD                   // 设置Path填充模式为 奇偶规则
        //path.setFillType(Path.FillType.INVERSE_EVEN_ODD);           // 反奇偶规则


        canvas.drawPath(path, mPaint!!)

    }

    /**
     * 测试 path 填充模式 非零环绕数规则
     *
     * @param canvas
     */
    private fun pathFillTypeWinding(canvas: Canvas) {

        mPaint!!.style = Paint.Style.FILL
        canvas.translate(width / 2.0f, height / 2.0f)
        mPaint!!.color = Color.BLACK

        val path = Path()

        // 添加小正方形 (通过这两行代码来控制小正方形边的方向,从而演示不同的效果)
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        //path.addRect(-200, -200, 200, 200, Path.Direction.CCW);


        // 添加大正方形
        path.addRect(-400f, -400f, 400f, 400f, Path.Direction.CCW)

        path.fillType = Path.FillType.WINDING


        canvas.drawPath(path, mPaint!!)

    }

    /**
     * @param canvas
     */
    private fun testPathLineTo(canvas: Canvas) {
        //移动到屏幕中间
        canvas.translate(width / 2.0f, height / 2.0f)
        mPaint!!.color = Color.BLACK
        val path = Path()
        path.lineTo(200f, 200f)
        //path.setLastPoint(200,100);//该方法会影响之前和之后的操作，去掉这行代码对比一下，就看出来了
        path.lineTo(200f, 0f)
        path.close()

        canvas.drawPath(path, mPaint!!)
    }


    /**
     * 测试 path 添加矩形
     *
     * @param canvas
     */
    private fun testPathAddRect(canvas: Canvas) {
        //移动到屏幕中间
        mPaint!!.color = Color.BLACK
        canvas.translate(width / 2.0f, height / 2.0f)
        val path = Path()
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        canvas.drawPath(path, mPaint!!)
    }


    /**
     * 测试 path.arcTo
     *
     * @param canvas
     */
    private fun testPathArcTo(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        //移动到屏幕中间
        mPaint.color = Color.BLACK
        val path = Path()

        var oval = RectF(0f, 0f, 80f, 80f)
        path.arcTo(oval, -180f, 90f)

        path.lineTo(measuredWidth - 80f, 0f)
        oval = RectF(measuredWidth - 80f, 0f, measuredWidth * 1.0f, 80f)
        path.arcTo(oval, -90f, 90f)

        path.lineTo(measuredWidth * 1.0f, measuredHeight - 80f)
        oval = RectF(measuredWidth - 80f, measuredHeight - 80f, measuredWidth * 1.0f, measuredHeight * 1.0f)
        path.arcTo(oval, 0f, 90f)

        path.lineTo(80f, measuredHeight * 1.0f)
        oval = RectF(0f, measuredHeight - 80f, 80f, measuredHeight * 1.0f)
        path.arcTo(oval, 90f, 90f)

        path.close()
        //canvas.drawPath(path, mPaint)
        canvas.clipPath(path)

        mPaint.color = Color.GREEN
        canvas.drawRect(RectF(0f, 0f, measuredWidth * 1.0f, measuredHeight * 1.0f), mPaint)

    }


    /**
     * 测试 path 添加弧形
     *
     * @param canvas
     */
    private fun testPathAddArc(canvas: Canvas) {
        //移动到屏幕中间
        mPaint!!.color = Color.BLACK
        canvas.translate(width / 2.0f, height / 2.0f)

        val path = Path()
        path.lineTo(100f, 100f)

        val oval = RectF(0f, 0f, 300f, 300f)
        path.addArc(oval, 0f, 270f)

        canvas.drawPath(path, mPaint!!)
    }

    /**
     * 合并path
     *
     * @param canvas
     */
    private fun testPathAddPath(canvas: Canvas) {
        //移动到屏幕中间
        mPaint!!.color = Color.BLACK
        canvas.translate(width / 2.0f, height / 2.0f)
        val path = Path()
        val src = Path()
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        src.addCircle(0f, 0f, 100f, Path.Direction.CW)

        path.addPath(src, 0f, 200f)

        canvas.drawPath(path, mPaint!!)
    }

    fun testPorterDuffMode(canvas: Canvas) {
        val count = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(dst, 0f, 0f, mPaint)
        mPaint!!.xfermode = xfermode
        canvas.drawBitmap(src, 0f, 0f, mPaint)
        mPaint!!.xfermode = null
        canvas.restoreToCount(count)
    }

    fun dp2px(dpVal: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal.toFloat(), resources.displayMetrics)
    }

    fun sp2px(dpVal: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal.toFloat(), resources.displayMetrics)
    }
}
