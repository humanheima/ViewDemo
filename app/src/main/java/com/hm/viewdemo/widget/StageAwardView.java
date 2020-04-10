package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.SendOptionsBean;
import com.hm.viewdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2020/4/9.
 * <p>
 * Desc: 整个控件的宽度 = 最左边的间距 + 三张图片的宽度 + 中间两个间距的宽度 + 最右边的间距
 * <p>
 * 整个控件的高度：每一行的高度（图片顶部到分钟底部的高度是56dp）* 行数 + 行数* 每行之间的空白距离（分钟底部到图片下一行图片顶部的高度）
 * <p>
 * 最左边的间距 =20dp
 * 三张图片的宽度 = 3* ivWidth
 * 中间两个间距的宽度 = 2 * horizontalSpace
 * <p>
 * 最右边的间距 =20dp
 * <p>
 * 图片宽高
 */
public class StageAwardView extends View {

    private static final String TAG = "StageAwardView";

    private int viewWidth;
    private int viewHeight;

    private Paint mPaint;

    private List<SendOptionsBean> stageList = new ArrayList<>();

    //已经领取的图片
    private Bitmap haveGotBitmap;
    //可以领取但是没有领取的图片
    private Bitmap canGetBitmap;
    //不能领取的图片
    private Bitmap cannotGetBitmap;

    //最左边的间距
    private int leftSpace = 0;
    private int rightSpace = 0;
    //图片的宽度
    private int ivWidth = 0;
    //一半图片的宽度
    private int halfIvWidth = 0;
    //图片高度
    private int ivHeight = 0;

    //两张图片之间的水平间隔
    private int horizontalSpace = 0;

    //Canvas在水平方向上translate的距离
    private int horizontalTranslateSpace;

    //两张图片之间的竖直间隔
    private int verticalSpace = 0;

    //Canvas在竖直方向上换行的时候translate的距离
    private int verticalTranslateSpace;

    //金币数量文字中心距离图片顶部的高度
    private int coinNumberCenterHeight;

    //图片顶部距离横线顶部的高度
    private int lineTopToIvTopHeight = 0;

    //横线绘制时候的画笔的高度
    private int strokeWidth = 0;

    //时间文字（分钟）中心距离图片顶部高度
    private int timeCenterHeight;

    //行数
    private int row;
    //列数
    private int column = 3;

    //每一行的高度（图片顶部到分钟底部的高度是56dp）
    private int rowHeight;

    //分钟底部到图片下一行图片顶部的高度35dp
    private int timeBottomToNextLineTop;


    private int coinColor;
    private int alphaCoinColor;
    private int timeColor;
    private int redLineColor;
    private int pinkLineColor;
    private Path pinkLinePaths = new Path();
    private Path redLinePath = new Path();

    private int cornerRadius;
    private RectF rectF = new RectF();

    //大圆点半径
    private float bigPointRadius;
    //小圆点半径
    private float smallPointRadius;

    private Path path0 = new Path();
    private Path path1 = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    private Path path4 = new Path();
    private Path path5 = new Path();
    private Path path6 = new Path();
    private Path path7 = new Path();
    private Path path8 = new Path();

    private List<Path> pathList = new ArrayList<>();

    public StageAwardView(Context context) {
        this(context, null);
    }

    public StageAwardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StageAwardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        coinColor = Color.parseColor("#FDFF00");
        alphaCoinColor = Color.parseColor("#7FFDFF00");
        timeColor = Color.parseColor("#999999");

        leftSpace = ScreenUtil.dpToPx(context, 20);
        rightSpace = leftSpace;
        cornerRadius = leftSpace / 2;

        coinNumberCenterHeight = ScreenUtil.dpToPx(context, 24);
        timeCenterHeight = ScreenUtil.dpToPx(context, 48);

        ivWidth = ScreenUtil.dpToPx(context, 24);
        halfIvWidth = ivWidth / 2;
        horizontalSpace = ScreenUtil.dpToPx(context, 57);
        horizontalTranslateSpace = (ivWidth + horizontalSpace);

        ivHeight = ScreenUtil.dpToPx(context, 30);
        verticalSpace = ScreenUtil.dpToPx(context, 60);
        verticalTranslateSpace = verticalSpace + ivHeight;

        lineTopToIvTopHeight = ScreenUtil.dpToPx(context, 36);

        strokeWidth = ScreenUtil.dpToPx(context, 3);

        bigPointRadius = strokeWidth;
        smallPointRadius = bigPointRadius / 3f;

        rowHeight = ScreenUtil.dpToPx(context, 56);
        timeBottomToNextLineTop = ScreenUtil.dpToPx(context, 35);

        viewWidth = leftSpace + 3 * ivWidth + 2 * horizontalSpace + rightSpace;
        //默认一行
        viewHeight = rowHeight * 2 + timeBottomToNextLineTop;

        int newWidth = ivWidth;
        int newHeight = ivHeight;

        haveGotBitmap = getNewBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_red_packet_have_got),
                newWidth, newHeight);
        canGetBitmap = getNewBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_red_pack),
                newWidth, newHeight);
        cannotGetBitmap = getNewBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.red_packet_cannot_get),
                newWidth, newHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redLineColor = Color.parseColor("#E83F46");
        pinkLineColor = Color.parseColor("#FFECEA");

        mPaint.setTextSize(ScreenUtil.spToPx(context, 10));
    }

    public void setStageList(List<SendOptionsBean> stageList) {
        this.stageList = stageList;
        int size = stageList.size();
        if (size % 3 == 0) {
            row = size / 3;
        } else {
            row = size / 3 + 1;
        }

        pathList.clear();

        viewHeight = rowHeight * row + timeBottomToNextLineTop * (row - 1);
        initPaths();

        //需要重新布局
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽高必须固定
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.GRAY);
        Log.d(TAG, "onDraw: row =" + row);

        drawBitmaps(canvas);

        /**
         * Canvas回到顶部
         */
        canvas.translate(0, -row * verticalTranslateSpace);

        if (row % 2 != 0) {//这种情况下，要向左移动
            canvas.translate(-2 * horizontalTranslateSpace, 0);
            canvas.translate(-leftSpace, 0);
        }
        canvas.translate(0, lineTopToIvTopHeight);

        drawPaths(canvas);

        drawPoints(canvas);
    }

    private void initPaths() {
        //宽度减去圆角半径，再减去曲线宽度的一半
        float rightEdge = viewWidth - cornerRadius - (strokeWidth / 2f);

        if (stageList.size() > 0) {
            path0.reset();
            path0.lineTo(leftSpace + halfIvWidth, 0);
            pathList.add(path0);
        }
        if (stageList.size() > 1) {
            path1.reset();
            path1.moveTo(leftSpace + halfIvWidth, 0);
            path1.lineTo(leftSpace + ivWidth + horizontalSpace + halfIvWidth, 0);
            pathList.add(path1);
        }
        if (stageList.size() > 2) {
            path2.reset();
            path2.moveTo(leftSpace + ivWidth + horizontalSpace + halfIvWidth, 0);
            path2.lineTo(leftSpace + 3 * ivWidth + 2 * horizontalSpace + cornerRadius - (strokeWidth / 2f), 0);
            pathList.add(path2);
        }
        /**
         * 这个要添加4段线
         */
        if (stageList.size() > 3) {
            path3.reset();
            path3.moveTo(rightEdge, 0);

            rectF.set(rightEdge - cornerRadius, 0, rightEdge + cornerRadius, 2 * cornerRadius);
            path3.arcTo(rectF, -90f, 90f);

            path3.lineTo(viewWidth - (strokeWidth / 2f), verticalTranslateSpace - cornerRadius);

            rectF.set(rightEdge - cornerRadius, verticalTranslateSpace - 2 * cornerRadius,
                    rightEdge + cornerRadius, verticalTranslateSpace);
            path3.arcTo(rectF, 0f, 90f);

            path3.lineTo(viewWidth - rightSpace - halfIvWidth, verticalTranslateSpace);
            pathList.add(path3);

        }

        if (stageList.size() > 4) {
            path4.reset();
            path4.moveTo(viewWidth - rightSpace - halfIvWidth, verticalTranslateSpace);
            path4.lineTo(leftSpace + horizontalSpace + ivWidth + halfIvWidth, verticalTranslateSpace);
            pathList.add(path4);
        }
        if (stageList.size() > 5) {
            path5.reset();
            path5.moveTo(leftSpace + horizontalSpace + ivWidth + halfIvWidth, verticalTranslateSpace);
            path5.lineTo(cornerRadius + (strokeWidth / 2f), verticalTranslateSpace);
            pathList.add(path5);
        }
        /**
         * 要添加4段线
         */
        if (stageList.size() > 6) {
            path6.reset();

            path6.moveTo(cornerRadius + (strokeWidth / 2f), verticalTranslateSpace);

            rectF.set((strokeWidth / 2f), verticalTranslateSpace,
                    2 * cornerRadius + (strokeWidth / 2f),
                    verticalTranslateSpace + 2 * cornerRadius);
            path6.arcTo(rectF, -90f, -90f);

            path6.lineTo((strokeWidth / 2f), 2 * verticalTranslateSpace - 2 * cornerRadius);

            rectF.set((strokeWidth / 2f), 2 * verticalTranslateSpace - 2 * cornerRadius,
                    2 * cornerRadius + (strokeWidth / 2f),
                    2 * verticalTranslateSpace);

            path6.arcTo(rectF, -180f, -90f);

            path6.lineTo(leftSpace + halfIvWidth, 2 * verticalTranslateSpace);
            pathList.add(path6);
        }

        if (stageList.size() > 7) {
            path7.reset();
            path7.moveTo(leftSpace + halfIvWidth, 2 * verticalTranslateSpace);
            path7.lineTo(leftSpace + ivWidth + horizontalSpace + halfIvWidth, 2 * verticalTranslateSpace);
            pathList.add(path7);
        }
        if (stageList.size() > 8) {
            path8.reset();
            path8.moveTo(leftSpace + ivWidth + horizontalSpace + halfIvWidth, 2 * verticalTranslateSpace);
            path8.lineTo(viewWidth, 2 * verticalTranslateSpace);
            pathList.add(path8);
        }
    }

    private void drawPaths(Canvas canvas) {
        pinkLinePaths.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        for (int i = 0; i < pathList.size(); i++) {
            Path path = pathList.get(i);
            if (i < stageList.size()) {
                SendOptionsBean optionsBean = stageList.get(i);
                if (optionsBean.haveGot() || optionsBean.canGet()) {
                    mPaint.setColor(redLineColor);
                } else {
                    mPaint.setColor(pinkLineColor);
                }
            }
            canvas.drawPath(path, mPaint);
        }
        // canvas.drawPath(pinkLinePaths, mPaint);
    }


    /**
     * 线绘制图片，金币数量，时间文字
     *
     * @param canvas
     */
    private void drawBitmaps(Canvas canvas) {
        for (int i = 0; i < row; i++) {
            if (i % 2 == 0) {//奇数行
                canvas.translate(leftSpace, 0);
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawBitmap(canvas, index);
                }

            } else {
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(-horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawBitmap(canvas, index);
                }
                canvas.translate(-leftSpace, 0);

            }
            canvas.translate(0, verticalTranslateSpace);
        }
    }

    private void drawBitmap(Canvas canvas, int index) {
        Log.d(TAG, "drawBitmap: index = " + index);
        if (index < stageList.size()) {
            SendOptionsBean optionsBean = stageList.get(index);
            if (optionsBean.canGet()) {
                canvas.drawBitmap(canGetBitmap, 0, 0, mPaint);
                mPaint.setColor(coinColor);
            } else if (optionsBean.haveGot()) {
                canvas.drawBitmap(haveGotBitmap, 0, 0, mPaint);
                mPaint.setColor(Color.WHITE);
            } else {
                canvas.drawBitmap(cannotGetBitmap, 0, 0, mPaint);
                mPaint.setColor(alphaCoinColor);
            }

            mPaint.setStyle(Paint.Style.FILL);

            String coinNumber = String.valueOf(optionsBean.getSegValue());

            canvas.translate(halfIvWidth, coinNumberCenterHeight);
            // 文字宽
            float textWidth = mPaint.measureText(coinNumber);
            // 文字baseline在y轴方向的位置
            float baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;

            canvas.drawText(coinNumber, -textWidth / 2, baseLineY, mPaint);

            canvas.translate(0, timeCenterHeight - coinNumberCenterHeight);

            mPaint.setColor(timeColor);
            String time = ((int) optionsBean.getSegKey()) + "分钟";

            textWidth = mPaint.measureText(time);
            baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;

            canvas.drawText(time, -textWidth / 2, baseLineY, mPaint);

            canvas.translate(-halfIvWidth, -timeCenterHeight);
        }
    }

    public Bitmap getNewBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap;
        try {
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            newBitmap = bitmap;
        }
        return newBitmap;
    }

    /**
     * 绘制大圆点和小圆点
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < row; i++) {
            if (i % 2 == 0) {//奇数行
                canvas.translate(leftSpace, 0);
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawPoint(canvas, index);
                }

            } else {
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(-horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawPoint(canvas, index);
                }
                canvas.translate(-leftSpace, 0);

            }
            canvas.translate(0, verticalTranslateSpace);
        }
    }

    private void drawPoint(Canvas canvas, int index) {
        Log.d(TAG, "drawPoint: index = " + index);
        if (index < stageList.size()) {
            canvas.translate(halfIvWidth, 0);
            SendOptionsBean optionsBean = stageList.get(index);
            if (optionsBean.canGet() || optionsBean.haveGot()) {
                mPaint.setColor(redLineColor);
            } else {
                mPaint.setColor(pinkLineColor);
            }
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(0, 0, bigPointRadius, mPaint);

            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(0, 0, smallPointRadius, mPaint);
            canvas.translate(-halfIvWidth, 0);
        }
    }

}
