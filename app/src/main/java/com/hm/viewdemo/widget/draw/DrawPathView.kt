package com.hm.viewdemo.widget.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import com.hm.viewdemo.R

/**
 * Crete by dumingwei on 2019-08-13
 * Desc: 学习 path 相关知识
 */
class DrawPathView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mPaint: Paint? = null
    private var mWidth = 0
    private var mHeight = 0
    private var currentValue = 0f // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private val pos = FloatArray(2) // 当前点的实际位置
    private val tan = FloatArray(2) // 当前点的tangent值,用于计算图片所需旋转的角度
    private val mBitmap // 箭头图片
            : Bitmap
    private val mMatrix // 矩阵,用于对图片进行一些操作
            : Matrix
    private val tempWidth: Int
    private val tempHeight: Int
    private var horizontalOffset = 0f
    private var verticalOffset = 0f

    //圆角弧度
    private var mCornerRadius = 12f

    //用户设置的elevation
    private var mRawShadowSize = 16f

    //用户设置的最大的elevation，不指定的话，就等于mRawShadowSize
    private var mRawMaxShadowSize = 16f
    private var mShadowSize = 25.5f

    //额外的阴影，默认是1dp
    private var mInsetShadow = 1f
    private val primaryColor: Int
    private val accentColor: Int
    private val mCardBounds = RectF()
    private var mPathLinearGradient: LinearGradient? = null
    private val mTempMatrix = Matrix()
    private val distance = 5
    private var translateX = 0
    private val mCornerShadowPath = Path()
    private val path = Path()
    private var mColorArray = intArrayOf(Color.RED, Color.BLUE)
    private fun buildComponents(bounds: Rect) {
        // Card is offset SHADOW_MULTIPLIER * maxShadowSize to account for the shadow shift.
        // We could have different top-bottom offsets to avoid extra gap above but in that case
        // center aligning Views inside the CardView would be problematic.
        val verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER
        mCardBounds[bounds.left + mRawMaxShadowSize, bounds.top + verticalOffset, bounds.right - mRawMaxShadowSize] =
            bounds.bottom - verticalOffset
        //buildShadowCorners();
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.color = Color.BLACK // 画笔颜色 - 黑色
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.textSize = sp2px(14).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    private val rectF = RectF()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制坐标系
        //mPaint.setColor(Color.RED);
        //mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStrokeWidth(3);
        //canvas.drawLine(0f, height / 5.0f, width * 1.0f, height / 5.0f, mPaint);
        //canvas.drawLine(width / 5.0f, 0f, width / 5.0f, height * 1.0f, mPaint);
        //rectF.set(width / 2.0f, height / 2.0f, width * 2 / 3f, height * 2 / 3f);

        //canvas.drawArc(rectF, 0f, 30f, false, mPaint);

        /*mPaint.setPathEffect(pathEffect);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        //canvas.drawCircle(300, 300, 200, mPaint);
        canvas.drawLine(40, 40, 400, 400, mPaint);*/

        //testPathLineTo(canvas);
        //testPathAddRect(canvas);
        pathAddRoundRect(canvas)
        //testPathAddPath(canvas);
        //pathAddArc(canvas);
        //testPathArcTo(canvas);
        //pathFillTypeEVenOld(canvas);
        //pathFillTypeWinding(canvas);
        //pathOp(canvas);
        //pathComputeBound(canvas);

        //getPathSegment(canvas);

        //getPathNextContour(canvas);
        //getPathPosStan(canvas);
        //getPathPosMatrix(canvas);

        //testArcTo(canvas);
        //testArcTo1(canvas);
        //testPathAddRect(canvas);
        //testArcTo2(canvas);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPathLinearGradient =
            LinearGradient(0f, 0f, w.toFloat(), 0f, mColorArray, null, Shader.TileMode.CLAMP)
    }

    private fun testArcTo(canvas: Canvas) {
        val path = Path()
        path.moveTo(10f, 10f)
        val rectF = RectF(100f, 10f, 200f, 100f)
        path.arcTo(rectF, 0f, 90f, true)
        canvas.drawPath(path, mPaint)
    }

    /**
     * path 调用addXXX系列函数
     *
     * @param canvas
     */
    private fun testArcTo1(canvas: Canvas) {
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 5f
        val path = Path()
        path.moveTo(10f, 10f)
        path.lineTo(100f, 50f)
        val rectF = RectF(100f, 100f, 150f, 150f)
        path.addArc(rectF, 0f, 90f)
        canvas.drawPath(path, mPaint)
    }

    /**
     * 根据路径方向布局文字
     *
     * @param canvas
     */
    private fun testArcTo2(canvas: Canvas) {
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 5f

        //第一条路径逆向生成
        val cCWRectPth = Path()
        val rectF1 = RectF(50f, 50f, 240f, 200f)
        cCWRectPth.addRect(rectF1, Path.Direction.CCW)

        //第2条顺时针生成
        val cWRectPth = Path()
        val rectF2 = RectF(290f, 50f, 480f, 200f)
        cWRectPth.addRect(rectF2, Path.Direction.CW)
        canvas.drawPath(cCWRectPth, mPaint)
        canvas.drawPath(cWRectPth, mPaint)
        val text = "苦心人天不负，有志者事竟成"
        mPaint!!.color = Color.GREEN
        mPaint!!.textSize = 35f
        canvas.drawTextOnPath(text, cCWRectPth, 0f, 18f, mPaint)
        canvas.drawTextOnPath(text, cWRectPth, 0f, 18f, mPaint)
    }

    private fun getPathPosMatrix(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = dp2px(1f).toFloat()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        val path = Path()
        path.addCircle(0f, 0f, 200f, Path.Direction.CW) // 添加一个圆形
        val measure = PathMeasure(path, false) // 创建 PathMeasure
        currentValue += 0.005.toFloat() // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0f
        }

        // 获取当前位置的坐标以及趋势的矩阵
        measure.getMatrix(
            measure.length * currentValue, mMatrix,
            PathMeasure.TANGENT_MATRIX_FLAG or PathMeasure.POSITION_MATRIX_FLAG
        )
        mMatrix.preTranslate(
            -mBitmap.width / 2f,
            -mBitmap.height / 2f
        ) // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)
        canvas.drawPath(path, mPaint) // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint) // 绘制箭头
        invalidate() // 重绘页面
    }

    private fun getPathPosStan(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = dp2px(1f).toFloat()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        val path = Path()
        path.addCircle(0f, 0f, 200f, Path.Direction.CW) // 添加一个圆形
        val measure = PathMeasure(path, false) // 创建 PathMeasure
        currentValue += 0.005.toFloat() // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0f
        }
        measure.getPosTan(measure.length * currentValue, pos, tan) // 获取当前位置的坐标以及趋势
        mMatrix.reset() // 重置Matrix
        val degrees = (Math.atan2(
            tan[1].toDouble(), tan[0].toDouble()
        ) * 180.0 / Math.PI).toFloat() // 计算图片旋转角度
        mMatrix.postRotate(degrees, mBitmap.width / 2f, mBitmap.height / 2f) // 旋转图片
        mMatrix.postTranslate(
            pos[0] - mBitmap.width / 2f,
            pos[1] - mBitmap.height / 2f
        ) // 将图片绘制中心调整到与当前点重合
        canvas.drawPath(path, mPaint) // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint) // 绘制箭头
        invalidate() // 重绘页面
    }

    private fun getPathNextContour(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = dp2px(1f).toFloat()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        val path = Path()
        path.addRect(-100f, -100f, 100f, 100f, Path.Direction.CW) // 添加小矩形
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CCW) // 添加大矩形
        canvas.drawPath(path, mPaint) // 绘制 Path
        val measure = PathMeasure(path, false) // 将Path与PathMeasure关联
        val len1 = measure.length // 获得第一条路径的长度
        measure.nextContour() // 跳转到下一条路径
        val len2 = measure.length // 获得第二条路径的长度
        Log.i(TAG, "len1=$len1") // 输出两条路径的长度
        Log.i(TAG, "len2=$len2")
    }

    private fun getPathSegment(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = dp2px(1f).toFloat()
        canvas.translate(mWidth / 2f, mHeight / 2f)
        val path = Path()
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)

        //先画出矩形
        canvas.drawPath(path, mPaint)
        mPaint!!.color = Color.BLUE
        val dst = Path()
        dst.lineTo(-300f, -300f)
        val measure = PathMeasure(path, true)

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        measure.getSegment(200f, 600f, dst, false)
        canvas.drawPath(dst, mPaint)
    }

    private fun pathComputeBound(canvas: Canvas) {
        canvas.translate(mWidth / 2f, mHeight / 2f)
        val rect1 = RectF() // 存放测量结果的矩形
        val path = Path() // 创建Path并添加一些内容
        path.lineTo(100f, -50f)
        path.lineTo(100f, 50f)
        path.close()
        path.addCircle(-100f, 0f, 100f, Path.Direction.CW)
        path.computeBounds(rect1, true) // 测量Path
        canvas.drawPath(path, mPaint) // 绘制Path
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.color = Color.RED
        canvas.drawRect(rect1, mPaint) // 绘制边界
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
        path1.addCircle(-x.toFloat(), 0f, r.toFloat(), Path.Direction.CW)
        path2.addCircle(x.toFloat(), 0f, r.toFloat(), Path.Direction.CW)
        pathOpResult.op(path1, path2, Path.Op.DIFFERENCE)
        canvas.translate(0f, 200f)
        canvas.drawText("DIFFERENCE", 240f, 0f, mPaint)
        canvas.drawPath(pathOpResult, mPaint)
        pathOpResult.op(path1, path2, Path.Op.REVERSE_DIFFERENCE)
        canvas.translate(0f, 300f)
        canvas.drawText("REVERSE_DIFFERENCE", 240f, 0f, mPaint)
        canvas.drawPath(pathOpResult, mPaint)
        pathOpResult.op(path1, path2, Path.Op.INTERSECT)
        canvas.translate(0f, 300f)
        canvas.drawText("INTERSECT", 240f, 0f, mPaint)
        canvas.drawPath(pathOpResult, mPaint)
        pathOpResult.op(path1, path2, Path.Op.UNION)
        canvas.translate(0f, 300f)
        canvas.drawText("UNION", 240f, 0f, mPaint)
        canvas.drawPath(pathOpResult, mPaint)
        pathOpResult.op(path1, path2, Path.Op.XOR)
        canvas.translate(0f, 300f)
        canvas.drawText("XOR", 240f, 0f, mPaint)
        canvas.drawPath(pathOpResult, mPaint)
    }

    /**
     * 测试 path 填充模式 奇偶规则
     * 测试发现 EVEN_ODD 和 INVERSE_EVEN_ODD 效果是一样的,出现这个原因是因为没有调用Path.close方法
     *
     * @param canvas
     */
    private fun pathFillTypeEVenOld(canvas: Canvas) {
        mPaint!!.style = Paint.Style.FILL
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        mPaint!!.color = Color.RED
        val path = Path()
        path.addRect(100f, 100f, 300f, 300f, Path.Direction.CW)
        path.addCircle(300f, 300f, 100f, Path.Direction.CW)
        path.close()

        //path.setFillType(Path.FillType.EVEN_ODD);                   // 设置Path填充模式为 奇偶规则
        //path.setFillType(Path.FillType.INVERSE_EVEN_ODD);           // 反奇偶规则
        canvas.drawPath(path, mPaint)
    }

    /**
     * 测试 path 填充模式 非零环绕数规则
     *
     * @param canvas
     */
    private fun pathFillTypeWinding(canvas: Canvas) {
        mPaint!!.style = Paint.Style.FILL
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        mPaint!!.color = Color.BLACK
        val path = Path()

        // 添加小正方形 (通过这两行代码来控制小正方形边的方向,从而演示不同的效果)
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        //path.addRect(-200, -200, 200, 200, Path.Direction.CCW);

        // 添加大正方形
        path.addRect(-400f, -400f, 400f, 400f, Path.Direction.CCW)
        path.fillType = Path.FillType.WINDING
        canvas.drawPath(path, mPaint)
    }

    /**
     * @param canvas
     */
    private fun testPathLineTo(canvas: Canvas) {
        //移动到屏幕中间
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        mPaint!!.color = Color.BLACK
        val path = Path()
        path.lineTo(200f, 200f)
        //path.setLastPoint(200,100);//该方法会影响之前和之后的操作，去掉这行代码对比一下，就看出来了
        path.lineTo(200f, 0f)
        path.close()
        canvas.drawPath(path, mPaint)
    }

    /**
     * 测试 path 添加矩形
     *
     * @param canvas
     */
    private fun testPathAddRect(canvas: Canvas) {
        //移动到屏幕中间
        mPaint!!.color = Color.BLACK
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        val path = Path()
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        canvas.drawPath(path, mPaint)
    }

    /**
     * 测试 path 添加圆角矩形
     *
     * @param canvas
     */
    private fun pathAddRoundRect(canvas: Canvas) {
        path.reset()
        //mPaint.setColor(primaryColor);
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = 10f
        //mPaint?.style = Paint.Style.FILL

        mPaint!!.shader = mPathLinearGradient

        val rectF2 = RectF(0f, 20f, width.toFloat(), 200f)
        val raddi = floatArrayOf(40f, 40f, 40f, 40f, 40f, 40f, 40f, 40f)
        path.addRoundRect(rectF2, raddi, Path.Direction.CW)
        //canvas.drawRoundRect(rectF2, 16f, 16f, mPaint)
        canvas.drawPath(path, mPaint)

        translateX += distance
        if (translateX > width) {
            translateX = -width
            mColorArray = mColorArray.reversedArray()
            mPathLinearGradient =
                LinearGradient(
                    0f,
                    0f,
                    width.toFloat(),
                    0f,
                    mColorArray,
                    null,
                    Shader.TileMode.CLAMP
                )
        }
        mTempMatrix.setTranslate(translateX.toFloat(), 0f)
        mPathLinearGradient!!.setLocalMatrix(mTempMatrix)
        invalidate()
    }

    private fun drawShadow(canvas: Canvas) {

//        //阴影上边界
//        final float edgeShadowTop = -mCornerRadius - mShadowSize;
//        final float inset = mCornerRadius + mInsetShadow + mRawShadowSize / 2;
//        //正常情况下应该成立
//        final boolean drawHorizontalEdges = mCardBounds.width() - 2 * inset > 0;
//        final boolean drawVerticalEdges = mCardBounds.height() - 2 * inset > 0;
//        // LT，绘制左上角的阴影
//        int saved = canvas.save();
//        canvas.translate(mCardBounds.left + inset, mCardBounds.top + inset);
//        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
//        if (drawHorizontalEdges) {
//            canvas.drawRect(0, edgeShadowTop,
//                    mCardBounds.width() - 2 * inset, -mCornerRadius,
//                    mEdgeShadowPaint);
//        }
//        canvas.restoreToCount(saved);
//        // RB
//        saved = canvas.save();
//        canvas.translate(mCardBounds.right - inset, mCardBounds.bottom - inset);
//        canvas.rotate(180f);
//        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
//        if (drawHorizontalEdges) {
//            canvas.drawRect(0, edgeShadowTop,
//                    mCardBounds.width() - 2 * inset, -mCornerRadius + mShadowSize,
//                    mEdgeShadowPaint);
//        }
//        canvas.restoreToCount(saved);
//        // LB
//        saved = canvas.save();
//        canvas.translate(mCardBounds.left + inset, mCardBounds.bottom - inset);
//        canvas.rotate(270f);
//        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
//        if (drawVerticalEdges) {
//            canvas.drawRect(0, edgeShadowTop,
//                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
//        }
//        canvas.restoreToCount(saved);
//        // RT
//        saved = canvas.save();
//        canvas.translate(mCardBounds.right - inset, mCardBounds.top + inset);
//        canvas.rotate(90f);
//        canvas.drawPath(mCornerShadowPath, mCornerShadowPaint);
//        if (drawVerticalEdges) {
//            canvas.drawRect(0, edgeShadowTop,
//                    mCardBounds.height() - 2 * inset, -mCornerRadius, mEdgeShadowPaint);
//        }
//        canvas.restoreToCount(saved);
    }

    /**
     * 测试 path.arcTo
     *
     * @param canvas
     */
    private fun testPathArcTo(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        //移动到屏幕中间
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        val path = Path()
        path.moveTo(10f, 10f)
        val oval = RectF(100f, 10f, 200f, 100f)
        //对比一下下面两行代码的区别
        //path.arcTo(oval, 0, 90);
        path.arcTo(oval, 0f, 90f, true)
        canvas.drawPath(path, mPaint)
    }

    /**
     * 测试 path 添加弧形
     *
     * @param canvas
     */
    private fun pathAddArc(canvas: Canvas) {
        mPaint!!.color = Color.BLACK
        mPaint!!.style = Paint.Style.STROKE
        //移动到屏幕中间
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        val path = Path()
        path.lineTo(100f, 100f)
        val oval = RectF(0f, 0f, 300f, 300f)
        path.addArc(oval, 0f, 270f)
        canvas.drawPath(path, mPaint)
    }

    /**
     * 合并path
     *
     * @param canvas
     */
    private fun testPathAddPath(canvas: Canvas) {
        //移动到屏幕中间
        mPaint!!.color = Color.BLACK
        canvas.translate(mWidth / 2.0f, mHeight / 2.0f)
        val path = Path()
        val src = Path()
        path.addRect(-200f, -200f, 200f, 200f, Path.Direction.CW)
        src.addCircle(0f, 0f, 100f, Path.Direction.CW)
        path.addPath(src, 0f, 200f)
        canvas.drawPath(path, mPaint)
    }

    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, resources.displayMetrics
        ).toInt()
    }

    fun sp2px(dpVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpVal.toFloat(), resources.displayMetrics
        ).toInt()
    }

    companion object {
        private const val TAG = "DrawPathView"
        private const val SHADOW_MULTIPLIER = 1.5f
    }

    init {
        initPaint()
        primaryColor = resources.getColor(R.color.colorPrimary)
        accentColor = resources.getColor(R.color.colorAccent)
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow)
        mMatrix = Matrix()
        tempWidth = dp2px(278f)
        tempHeight = dp2px(150f)
        mInsetShadow = dp2px(1f).toFloat()
        mCornerRadius = dp2px(12f).toFloat()
        mRawShadowSize = dp2px(16f).toFloat()
        mRawMaxShadowSize = dp2px(16f).toFloat()
        mShadowSize = SHADOW_MULTIPLIER * mRawMaxShadowSize + mInsetShadow + 0.5f
        horizontalOffset = mRawMaxShadowSize
        verticalOffset = mRawMaxShadowSize * SHADOW_MULTIPLIER
    }
}