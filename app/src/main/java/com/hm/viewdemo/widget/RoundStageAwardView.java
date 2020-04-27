package com.hm.viewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.bean.SendOptionsBean;
import com.hm.viewdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2020/4/10.
 * <p>
 * Desc: 转角需要5段线
 * 第一段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14
 * 第二段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
 * 第三段直线的长度 verticalTranslateSpace - 2* cornerRadius = 90 -20 =70f 占 0.48
 * 第四段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
 * 第五段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14。但是计算的时候要认为是占0.18，这样这五条线加起来的的百分比才能达到1。
 * <p>
 * 同理，左边的也一样
 */
public class RoundStageAwardView extends View {

    private static final String TAG = "StageAwardView";

    private final float THRESHOLD = 0.001f;

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

    private int cornerRadius;

    //大圆点半径
    private float bigPointRadius;
    //小圆点半径
    private float smallPointRadius;

    //第一个未完成的阶段
    private int firstNotArriveStage = 0;

    //第一个未完成区间的完成度
    private float finishDegree = 0;


    private PosInfo posInfo0 = new PosInfo();
    private PosInfo posInfo1 = new PosInfo();
    private PosInfo posInfo2 = new PosInfo();
    private PosInfo posInfo3 = new PosInfo();
    private PosInfo posInfo4 = new PosInfo();
    private PosInfo posInfo5 = new PosInfo();
    private PosInfo posInfo6 = new PosInfo();
    private PosInfo posInfo7 = new PosInfo();
    private PosInfo posInfo8 = new PosInfo();
    private PosInfo posInfo9 = new PosInfo();
    private PosInfo posInfo10 = new PosInfo();
    private PosInfo posInfo11 = new PosInfo();
    private PosInfo posInfo12 = new PosInfo();

    private ArcInfo arcInfo0 = new ArcInfo();
    private ArcInfo arcInfo1 = new ArcInfo();
    private ArcInfo arcInfo2 = new ArcInfo();
    private ArcInfo arcInfo3 = new ArcInfo();


    private List<PosInfo> posInfoList = new ArrayList<>();
    private List<ArcInfo> arcInfoList = new ArrayList<>();

    public RoundStageAwardView(Context context) {
        this(context, null);
    }

    public RoundStageAwardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundStageAwardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        coinColor = Color.parseColor("#FDFF00");
        //alphaCoinColor = Color.parseColor("#7FFDFF00");
        alphaCoinColor = coinColor;
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
        canGetBitmap = getNewBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_red_pack_can_get),
                newWidth, newHeight);
        cannotGetBitmap = getNewBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_red_packet_cannot_get),
                newWidth, newHeight);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redLineColor = Color.parseColor("#E83F46");
        pinkLineColor = Color.parseColor("#FFECEA");

        mPaint.setTextSize(ScreenUtil.spToPx(context, 10));
    }


    /**
     * @param stageList           阶段列表
     * @param firstNotArriveStage 第一个未完成的区间，在stageList的下标
     * @param finishDegree        未完成区间的完成度
     */
    public void setData(List<SendOptionsBean> stageList, int firstNotArriveStage, float finishDegree) {
        this.stageList = stageList;
        int size = stageList.size();
        if (size % 3 == 0) {
            row = size / 3;
        } else {
            row = size / 3 + 1;
        }

        viewHeight = rowHeight * row + timeBottomToNextLineTop * (row - 1);
        initLines();

        this.firstNotArriveStage = firstNotArriveStage;
        if (finishDegree - 1f >= THRESHOLD) {
            finishDegree = 1f;
        }
        this.finishDegree = finishDegree;

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

        drawBitmapCoinNumberTimes(canvas);

        /**
         * Canvas回到顶部
         */
        canvas.translate(0, -row * verticalTranslateSpace);

        if (row % 2 != 0) {//这种情况下，要向左移动
            canvas.translate(-2 * horizontalTranslateSpace, 0);
            canvas.translate(-leftSpace, 0);
        }
        canvas.translate(0, lineTopToIvTopHeight);

        //drawPaths(canvas);
        drawPinkLines(canvas);
        drawRedLines(canvas);

        drawPoints(canvas);
    }

    private void drawPinkLines(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(pinkLineColor);
        for (PosInfo posInfo : posInfoList) {
            canvas.drawLine(posInfo.startX, posInfo.startY, posInfo.endX, posInfo.endY, mPaint);
        }

        for (ArcInfo arcInfo : arcInfoList) {
            canvas.drawArc(arcInfo.rectF, arcInfo.startAngle, arcInfo.sweepAngle, false, mPaint);
        }
    }

    private void drawRedLines(Canvas canvas) {
        int size = stageList.size();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(redLineColor);

        if (firstNotArriveStage >= size) {
            //阶段全部完成
            for (PosInfo posInfo : posInfoList) {
                canvas.drawLine(posInfo.startX, posInfo.startY, posInfo.endX, posInfo.endY, mPaint);
            }
            for (ArcInfo arcInfo : arcInfoList) {
                canvas.drawArc(arcInfo.rectF, arcInfo.startAngle, arcInfo.sweepAngle, false, mPaint);
            }

        } else {
            if (firstNotArriveStage == 0) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX * finishDegree, posInfo0.endY, mPaint);

            } else if (firstNotArriveStage == 1) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.startX + (posInfo1.endX - posInfo1.startX) * finishDegree, posInfo1.endY, mPaint);

            } else if (firstNotArriveStage == 2) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.startX + (posInfo2.endX - posInfo2.startX) * finishDegree, posInfo2.endY, mPaint);

            } else if (firstNotArriveStage == 3) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                //第一段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14
                //第二段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
                //第三段直线的长度 verticalTranslateSpace - 2* cornerRadius = 90 -20 =70f 占 0.48
                //第四段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
                //第五段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14
                if (finishDegree < 0.14) {
                    //posInfo3 占 posInfo3 posInfo4 posInfo5 总长的 0.14
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.startX + (posInfo3.endX - posInfo3.startX) * finishDegree / 0.14f,
                            posInfo3.endY, mPaint);
                } else if (finishDegree < 0.24) {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);

                    canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                            arcInfo0.sweepAngle * ((finishDegree - 0.14f) / 0.1f), false, mPaint);

                } else if (finishDegree < 0.72) {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);

                    canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                            arcInfo0.sweepAngle, false, mPaint);

                    canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                            posInfo4.startY + (posInfo4.endY - posInfo4.startY) * ((finishDegree - 0.24f) / 0.48f), mPaint);


                } else if (finishDegree < 0.82) {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);

                    canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                            arcInfo0.sweepAngle, false, mPaint);

                    canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                            posInfo4.endY, mPaint);

                    canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle,
                            arcInfo1.sweepAngle * ((finishDegree - 0.72f) / 0.1f), false, mPaint);

                } else {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);

                    canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                            arcInfo0.sweepAngle, false, mPaint);

                    canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                            posInfo4.endY, mPaint);

                    canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                    //todo 计算的时候认为这段线占比为0.18
                    canvas.drawLine(posInfo5.startX, posInfo5.startY,
                            posInfo5.startX - (posInfo5.startX - posInfo5.endX) * ((finishDegree - 0.82f) / 0.18f),
                            posInfo5.endY, mPaint);
                }

            } else if (firstNotArriveStage == 4) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);

                canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                        arcInfo0.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY,
                        posInfo5.endX,
                        posInfo5.endY, mPaint);

                canvas.drawLine(posInfo6.startX, posInfo6.startY,
                        posInfo6.startX - (posInfo6.startX - posInfo6.endX) * finishDegree,
                        posInfo6.endY, mPaint);

            } else if (firstNotArriveStage == 5) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);

                canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                        arcInfo0.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY,
                        posInfo5.endX,
                        posInfo5.endY, mPaint);

                canvas.drawLine(posInfo6.startX, posInfo6.startY,
                        posInfo6.endX,
                        posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY,
                        posInfo7.startX - (posInfo7.startX - posInfo7.endX) * finishDegree,
                        posInfo7.endY, mPaint);

            } else if (firstNotArriveStage == 6) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);

                canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                        arcInfo0.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY,
                        posInfo5.endX,
                        posInfo5.endY, mPaint);

                canvas.drawLine(posInfo6.startX, posInfo6.startY,
                        posInfo6.endX,
                        posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);


                if (finishDegree < 0.14) {
                    //posInfo8 占 posInfo8 posInfo4 posInfo5 总长的 0.14
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.startX - (posInfo8.startX - posInfo8.endX) * finishDegree / 0.14f,
                            posInfo8.endY, mPaint);
                } else if (finishDegree < 0.24) {
                    canvas.drawLine(posInfo8.startX, posInfo8.startY, posInfo8.endX, posInfo8.endY, mPaint);

                    canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                            arcInfo2.sweepAngle * ((finishDegree - 0.14f) / 0.1f), false, mPaint);
                } else if (finishDegree < 0.72) {
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.endX,
                            posInfo8.endY, mPaint);
                    //弧线画满
                    canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                            arcInfo2.sweepAngle, false, mPaint);

                    //posInfo9 占 posInfo8 posInfo9 posInfo10 总长的60%
                    canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX,
                            posInfo9.startY + (posInfo9.endY - posInfo9.startY) * ((finishDegree - 0.24f) / 0.48f), mPaint);

                } else if (finishDegree < 0.82) {
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.endX,
                            posInfo8.endY, mPaint);
                    //弧线画满
                    canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                            arcInfo2.sweepAngle, false, mPaint);

                    canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX, posInfo9.endY, mPaint);

                    canvas.drawArc(arcInfo3.rectF, arcInfo3.startAngle,
                            arcInfo3.sweepAngle * ((finishDegree - 0.72f) / 0.1f), false, mPaint);
                } else {

                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.endX,
                            posInfo8.endY, mPaint);
                    //弧线画满
                    canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                            arcInfo2.sweepAngle, false, mPaint);

                    canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX, posInfo9.endY, mPaint);

                    canvas.drawArc(arcInfo3.rectF, arcInfo3.startAngle,
                            arcInfo3.sweepAngle, false, mPaint);

                    //认为这段线占0.18
                    canvas.drawLine(posInfo10.startX, posInfo10.startY,
                            posInfo10.startX + (posInfo10.endX - posInfo10.startX) * ((finishDegree - 0.82f) / 0.18f),
                            posInfo10.endY, mPaint);
                }


            } else if (firstNotArriveStage == 7) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);

                canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                        arcInfo0.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY,
                        posInfo5.endX,
                        posInfo5.endY, mPaint);

                canvas.drawLine(posInfo6.startX, posInfo6.startY,
                        posInfo6.endX,
                        posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);

                canvas.drawLine(posInfo8.startX, posInfo8.startY,
                        posInfo8.endX,
                        posInfo8.endY, mPaint);
                //弧线画满
                canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                        arcInfo2.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX, posInfo9.endY, mPaint);

                canvas.drawArc(arcInfo3.rectF, arcInfo3.startAngle,
                        arcInfo3.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo10.startX, posInfo10.startY, posInfo10.endX, posInfo10.endY, mPaint);

                canvas.drawLine(posInfo11.startX, posInfo11.startY,
                        posInfo11.startX + (posInfo11.endX - posInfo11.startX) * finishDegree, posInfo11.endY, mPaint);

            } else if (firstNotArriveStage == 8) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);

                canvas.drawArc(arcInfo0.rectF, arcInfo0.startAngle,
                        arcInfo0.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawArc(arcInfo1.rectF, arcInfo1.startAngle, arcInfo1.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY,
                        posInfo5.endX,
                        posInfo5.endY, mPaint);

                canvas.drawLine(posInfo6.startX, posInfo6.startY,
                        posInfo6.endX,
                        posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);

                canvas.drawLine(posInfo8.startX, posInfo8.startY,
                        posInfo8.endX,
                        posInfo8.endY, mPaint);
                //弧线画满
                canvas.drawArc(arcInfo2.rectF, arcInfo2.startAngle,
                        arcInfo2.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX, posInfo9.endY, mPaint);

                canvas.drawArc(arcInfo3.rectF, arcInfo3.startAngle,
                        arcInfo3.sweepAngle, false, mPaint);

                canvas.drawLine(posInfo10.startX, posInfo10.startY, posInfo10.endX, posInfo10.endY, mPaint);

                canvas.drawLine(posInfo11.startX, posInfo11.startY, posInfo11.endX, posInfo11.endY, mPaint);

                canvas.drawLine(posInfo12.startX, posInfo12.startY,
                        posInfo12.startX + (posInfo12.endX - posInfo12.startX) * finishDegree, posInfo12.endY, mPaint);

            }
        }


    }

    private void initLines() {
        posInfoList.clear();

        arcInfoList.clear();

        //第一张图片中心X
        int firstIvCenterX = leftSpace + halfIvWidth;

        //第二张图片中心X
        int secondIvCenterX = leftSpace + ivWidth + horizontalSpace + halfIvWidth;

        //第3张图片中心X
        int thirdIvCenterX = leftSpace + 2 * ivWidth + 2 * horizontalSpace + halfIvWidth;

        //thirdIvCenterX加上圆角半径，再减去曲线宽度的一半
        float rightEdge = thirdIvCenterX + halfIvWidth + cornerRadius - (strokeWidth / 2f);
        //float rightEdge = viewWidth - cornerRadius - (strokeWidth / 2f);

        if (stageList.size() > 0) {
            //第一段线
            posInfo0.startX = 0;
            posInfo0.startY = 0;

            posInfo0.endX = firstIvCenterX;
            posInfo0.endY = 0;

            posInfoList.add(posInfo0);

        }
        if (stageList.size() > 1) {

            posInfo1.startX = firstIvCenterX;
            posInfo1.startY = 0;

            posInfo1.endX = secondIvCenterX;
            posInfo1.endY = 0;

            posInfoList.add(posInfo1);

        }
        if (stageList.size() > 2) {

            posInfo2.startX = secondIvCenterX;
            posInfo2.startY = 0;

            posInfo2.endX = thirdIvCenterX;
            posInfo2.endY = 0;

            posInfoList.add(posInfo2);
        }
        /**
         * 这个圆弧要添加5段线
         * 直角的话只需要添加3条线
         */
        if (stageList.size() > 3) {
            //第一段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14
            //第二段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
            //第三段直线的长度 verticalTranslateSpace - 2* cornerRadius = 90 -20 =70f 占 0.48
            //第四段弧线的长度  2 * pi * cornerRadius * 1/4 = 5pi = 15.6f 占 0.1
            //第五段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f 占0.14
            //同理，左边的也一样

            posInfo3.startX = thirdIvCenterX;
            posInfo3.startY = 0;

            //第一段线的距离 halfIvWidth + cornerRadius - (strokeWidth / 2f) = 12 + 10 -1.5 =20.5f
            posInfo3.endX = rightEdge;
            posInfo3.endY = 0;
            posInfoList.add(posInfo3);

            arcInfo0.rectF.set(rightEdge - cornerRadius, 0, rightEdge + cornerRadius, 2 * cornerRadius);
            arcInfo0.startAngle = -90f;
            arcInfo0.sweepAngle = 90f;

            arcInfoList.add(arcInfo0);

            posInfo4.startX = viewWidth - strokeWidth / 2f;
            posInfo4.startY = cornerRadius;

            posInfo4.endX = viewWidth - strokeWidth / 2f;
            posInfo4.endY = verticalTranslateSpace - cornerRadius;
            posInfoList.add(posInfo4);

            arcInfo1.rectF.set(rightEdge - cornerRadius, verticalTranslateSpace - 2 * cornerRadius,
                    rightEdge + cornerRadius, verticalTranslateSpace);

            arcInfo1.startAngle = 0f;
            arcInfo1.sweepAngle = 90f;

            arcInfoList.add(arcInfo1);

            posInfo5.startX = rightEdge;
            posInfo5.startY = verticalTranslateSpace;

            posInfo5.endX = thirdIvCenterX;
            posInfo5.endY = verticalTranslateSpace;

            posInfoList.add(posInfo5);

        }

        if (stageList.size() > 4) {

            posInfo6.startX = thirdIvCenterX;
            posInfo6.startY = verticalTranslateSpace;

            posInfo6.endX = secondIvCenterX;
            posInfo6.endY = verticalTranslateSpace;

            posInfoList.add(posInfo6);

        }
        float leftEdge = cornerRadius + (strokeWidth / 2f);

        if (stageList.size() > 5) {

            posInfo7.startX = secondIvCenterX;
            posInfo7.startY = verticalTranslateSpace;

            posInfo7.endX = firstIvCenterX;
            posInfo7.endY = verticalTranslateSpace;

            posInfoList.add(posInfo7);

        }
        /**
         * 圆角需要添加5条线
         * 直角的话只需要添加3条线
         */
        if (stageList.size() > 6) {

            posInfo8.startX = firstIvCenterX;
            posInfo8.startY = verticalTranslateSpace;

            posInfo8.endX = leftEdge;
            posInfo8.endY = verticalTranslateSpace;

            posInfoList.add(posInfo8);

            arcInfo2.rectF.set((strokeWidth / 2f), verticalTranslateSpace,
                    2 * cornerRadius + (strokeWidth / 2f),
                    verticalTranslateSpace + 2 * cornerRadius);

            arcInfo2.startAngle = -90f;
            arcInfo2.sweepAngle = -90f;

            arcInfoList.add(arcInfo2);


            posInfo9.startX = strokeWidth / 2f;
            posInfo9.startY = verticalTranslateSpace + cornerRadius;

            posInfo9.endX = strokeWidth / 2f;
            posInfo9.endY = 2 * verticalTranslateSpace - cornerRadius;

            posInfoList.add(posInfo9);


            arcInfo3.rectF.set((strokeWidth / 2f), 2 * verticalTranslateSpace - 2 * cornerRadius,
                    2 * cornerRadius + (strokeWidth / 2f),
                    2 * verticalTranslateSpace);

            arcInfo3.startAngle = -180f;
            arcInfo3.sweepAngle = -90f;

            arcInfoList.add(arcInfo3);

            posInfo10.startX = leftEdge;
            posInfo10.startY = 2 * verticalTranslateSpace;

            posInfo10.endX = firstIvCenterX;
            posInfo10.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo10);
        }

        if (stageList.size() > 7) {
            posInfo11.startX = firstIvCenterX;
            posInfo11.startY = 2 * verticalTranslateSpace;

            posInfo11.endX = secondIvCenterX;
            posInfo11.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo11);

        }
        if (stageList.size() > 8) {
            posInfo12.startX = secondIvCenterX;
            posInfo12.startY = 2 * verticalTranslateSpace;

            posInfo12.endX = thirdIvCenterX;
            posInfo12.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo12);
        }
    }

    /**
     * 线绘制图片，金币数量，时间文字
     *
     * @param canvas
     */
    private void drawBitmapCoinNumberTimes(Canvas canvas) {
        for (int i = 0; i < row; i++) {
            if (i % 2 == 0) {//奇数行
                canvas.translate(leftSpace, 0);
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawSingleItem(canvas, index);
                }

            } else {
                for (int j = 0; j < column; j++) {
                    if (j != 0) {
                        canvas.translate(-horizontalTranslateSpace, 0);
                    }
                    int index = i * column + j;
                    drawSingleItem(canvas, index);
                }
                canvas.translate(-leftSpace, 0);

            }
            canvas.translate(0, verticalTranslateSpace);
        }
    }

    private void drawSingleItem(Canvas canvas, int index) {
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
            double segKey = optionsBean.getSegKey();

            String time;
            if (segKey < 1) {
                time = (int) (segKey * 60) + "秒";
            } else {
                time = segKey + "分钟";
            }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent:  width = " + getWidth() + " , height = " + getHeight());
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent:  ACTION_DOWN x = " + event.getX() + " , y =" + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent:  ACTION_UP x = " + event.getX() + " , y =" + event.getY());
                getClickIndex(event.getX(), event.getY());
                break;
            default:
                break;

        }
        return true;

    }

    /**
     * 根据点击的坐标获取点击区域
     *
     * @param x up事件时候的x坐标
     * @param y up事件时候的y坐标
     */
    public int getClickIndex(float x, float y) {
        int index;
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return -1;
        }
        if (x <= 0 || x >= width || y <= 0 || y >= height) {
            return -1;
        }

        int oneOfThreeWidth = width / 3;
        int twoOfThreeWidth = oneOfThreeWidth * 2;

        int oneOfThreeHeight = height / 3;
        int twoOfThreeHeight = oneOfThreeHeight * 2;

        if (y < oneOfThreeHeight) {
            if (x < oneOfThreeWidth) {
                index = 0;

            } else if (x < twoOfThreeWidth) {
                index = 1;

            } else {
                index = 2;
            }
        } else if (y < twoOfThreeHeight) {
            //注意这行的index是反着来的
            if (x > twoOfThreeHeight) {
                index = 3;

            } else if (x > oneOfThreeHeight) {
                index = 4;
            } else {
                index = 5;
            }
        } else {
            if (x < oneOfThreeWidth) {
                index = 6;
            } else if (x < twoOfThreeWidth) {
                index = 7;
            } else {
                index = 8;
            }
        }

        Toast.makeText(getContext(), "点击区域是" + index, Toast.LENGTH_SHORT).show();

        return index;
    }

    /**
     * 保存每一条线的起点和终点信息
     */
    static class PosInfo {
        float startX;
        float startY;

        float endX;
        float endY;
    }

    static class ArcInfo {

        RectF rectF = new RectF();

        float startAngle;
        float sweepAngle;

    }
}
