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
import androidx.annotation.Nullable;
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
 * Desc: 整个控件的宽度 = 最左边的间距 + 三张图片的宽度 + 中间两个间距的宽度 + 最右边的间距
 * <p>
 * 整个控件的高度：每一行的高度（图片顶部到分钟底部的高度是56dp）* 行数 + 行数* 每行之间的空白距离（分钟底部到图片下一行图片顶部的高度）
 * <p>
 * 最左边的间距 =20dp
 * 三张图片的宽度 = 3* ivWidth
 * 中间两个间距的宽度 = 2 * horizontalSpace
 * <p>
 * 先按照直角来做
 * <p>
 * 图片宽高
 */
public class StageAwardViewCopy extends View {

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

    //第一个未完成的阶段
    private int firstNotArriveStage = 0;

    //第一个未完成区间的完成度
    private float finishDegree = 0;

    private Path path0 = new Path();
    private Path path1 = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    private Path path4 = new Path();
    private Path path5 = new Path();
    private Path path6 = new Path();
    private Path path7 = new Path();
    private Path path8 = new Path();


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

    private List<Path> pathList = new ArrayList<>();
    private List<PosInfo> posInfoList = new ArrayList<>();

    public StageAwardViewCopy(Context context) {
        this(context, null);
    }

    public StageAwardViewCopy(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StageAwardViewCopy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        pathList.clear();

        viewHeight = rowHeight * row + timeBottomToNextLineTop * (row - 1);
        initPaths();

        this.firstNotArriveStage = firstNotArriveStage;
        this.finishDegree = finishDegree;

        //需要重新布局
        requestLayout();
        invalidate();
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
        canvas.drawColor(Color.GRAY);
        Log.i(TAG, "onDraw: row =" + row);

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
        } else {

            if (firstNotArriveStage == 0) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX * finishDegree, posInfo0.endY, mPaint);
            } else if (firstNotArriveStage == 1) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX * finishDegree, posInfo1.endY, mPaint);

            } else if (firstNotArriveStage == 2) {

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY * finishDegree, mPaint);

            } else if (firstNotArriveStage == 3) {
                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                if (finishDegree < 0.2) {
                    //posInfo3 占 posInfo3 posInfo4 posInfo5 总长的20%
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.startX + (posInfo3.endX - posInfo3.startX) * finishDegree * 5,
                            posInfo3.endY, mPaint);
                } else if (finishDegree < 0.8) {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);
                    //posInfo4 占 posInfo3 posInfo4 posInfo5 总长的60%
                    canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                            posInfo4.startY + (posInfo4.endY - posInfo4.startY) * (finishDegree - 0.2f) * 5 / 3, mPaint);
                } else {
                    canvas.drawLine(posInfo3.startX, posInfo3.startY,
                            posInfo3.endX,
                            posInfo3.endY, mPaint);
                    canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                            posInfo4.endY, mPaint);

                    //posInfo5 占 posInfo3 posInfo4 posInfo5 总长的20%
                    canvas.drawLine(posInfo5.startX, posInfo5.startY,
                            posInfo5.startX - (posInfo5.startX - posInfo5.endX) * (finishDegree - 0.8f) * 5,
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
                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY, posInfo5.endX,
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
                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY, posInfo5.endX,
                        posInfo5.endY, mPaint);


                canvas.drawLine(posInfo6.startX, posInfo6.startY, posInfo6.endX, posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY,
                        posInfo7.startX - (posInfo7.startX - posInfo7.endX) * finishDegree,
                        posInfo7.endY, mPaint);

            } else if (firstNotArriveStage == 6) {
                //处理三段线段

                canvas.drawLine(posInfo0.startX, posInfo0.startY,
                        posInfo0.endX, posInfo0.endY, mPaint);

                canvas.drawLine(posInfo1.startX, posInfo1.startY,
                        posInfo1.endX, posInfo1.endY, mPaint);

                canvas.drawLine(posInfo2.startX, posInfo2.startY,
                        posInfo2.endX, posInfo2.endY, mPaint);

                canvas.drawLine(posInfo3.startX, posInfo3.startY,
                        posInfo3.endX,
                        posInfo3.endY, mPaint);
                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY, posInfo5.endX,
                        posInfo5.endY, mPaint);


                canvas.drawLine(posInfo6.startX, posInfo6.startY, posInfo6.endX, posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);


                if (finishDegree < 0.2) {
                    //posInfo8 占 posInfo8 posInfo9 posInfo10 总长的20%
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.startX - (posInfo8.startX - posInfo8.endX) * finishDegree * 5,
                            posInfo8.endY, mPaint);

                } else if (finishDegree < 0.8) {
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.endX,
                            posInfo8.endY, mPaint);
                    //posInfo9 占 posInfo8 posInfo9 posInfo10 总长的60%
                    canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX,
                            posInfo9.startY + (posInfo9.endY - posInfo9.startY) * (finishDegree - 0.2f) * 5 / 3, mPaint);
                } else {
                    canvas.drawLine(posInfo8.startX, posInfo8.startY,
                            posInfo8.endX,
                            posInfo8.endY, mPaint);
                    canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.startX,
                            posInfo9.endY, mPaint);

                    //posInfo10 占 posInfo8 posInfo9 posInfo10 总长的20%
                    canvas.drawLine(posInfo10.startX, posInfo10.startY,
                            posInfo10.startX + (posInfo10.endX - posInfo10.startX) * (finishDegree - 0.8f) * 5,
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
                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY, posInfo5.endX,
                        posInfo5.endY, mPaint);


                canvas.drawLine(posInfo6.startX, posInfo6.startY, posInfo6.endX, posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);

                canvas.drawLine(posInfo8.startX, posInfo8.startY, posInfo8.endX, posInfo8.endY, mPaint);
                canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.endX, posInfo9.endY, mPaint);
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
                canvas.drawLine(posInfo4.startX, posInfo4.startY, posInfo4.startX,
                        posInfo4.endY, mPaint);

                canvas.drawLine(posInfo5.startX, posInfo5.startY, posInfo5.endX,
                        posInfo5.endY, mPaint);


                canvas.drawLine(posInfo6.startX, posInfo6.startY, posInfo6.endX, posInfo6.endY, mPaint);

                canvas.drawLine(posInfo7.startX, posInfo7.startY, posInfo7.endX, posInfo7.endY, mPaint);

                canvas.drawLine(posInfo8.startX, posInfo8.startY, posInfo8.endX, posInfo8.endY, mPaint);
                canvas.drawLine(posInfo9.startX, posInfo9.startY, posInfo9.endX, posInfo9.endY, mPaint);
                canvas.drawLine(posInfo10.startX, posInfo10.startY, posInfo10.endX, posInfo10.endY, mPaint);

                canvas.drawLine(posInfo11.startX, posInfo11.startY, posInfo11.endX, posInfo11.endY, mPaint);

                canvas.drawLine(posInfo12.startX, posInfo12.startY,
                        posInfo12.startX + (posInfo12.endX - posInfo12.startX) * finishDegree, posInfo12.endY, mPaint);

            }
        }


    }

    private void initPaths() {
        //宽度减去圆角半径，再减去曲线宽度的一半
        float rightEdge = viewWidth - cornerRadius - (strokeWidth / 2f);

        //第一张图片中心X
        int firstIvCenterX = leftSpace + halfIvWidth;

        //第二张图片中心X
        int secondIvCenterX = leftSpace + ivWidth + horizontalSpace + halfIvWidth;

        //第3张图片中心X
        int thirdIvCenterX = leftSpace + 2 * ivWidth + 2 * horizontalSpace + halfIvWidth;

        if (stageList.size() > 0) {
            path0.reset();
            path0.lineTo(firstIvCenterX, 0);
            pathList.add(path0);

            //第一段线
            posInfo0.startX = 0;
            posInfo0.startY = 0;

            posInfo0.endX = firstIvCenterX;
            posInfo0.endY = 0;

            posInfoList.add(posInfo0);

        }
        if (stageList.size() > 1) {
            path1.reset();
            path1.moveTo(firstIvCenterX, 0);
            path1.lineTo(secondIvCenterX, 0);
            pathList.add(path1);

            posInfo1.startX = firstIvCenterX;
            posInfo1.startY = 0;

            posInfo1.endX = secondIvCenterX;
            posInfo1.endY = 0;

            posInfoList.add(posInfo1);

        }
        if (stageList.size() > 2) {
            path2.reset();
            path2.moveTo(secondIvCenterX, 0);
            path2.lineTo(thirdIvCenterX, 0);
            pathList.add(path2);

            posInfo2.startX = secondIvCenterX;
            posInfo2.startY = 0;

            posInfo2.endX = thirdIvCenterX;
            posInfo2.endY = 0;

            posInfoList.add(posInfo2);
        }
        /**
         * 这个要添加5段线
         * 直角的话只需要添加3条线
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

            posInfo3.startX = thirdIvCenterX;
            posInfo3.startY = 0;

            //thirdIvCenterX + halfIvWidth + rightSpace - strokeWidth / 2f
            //这条线的长度是 halfIvWidth + rightSpace - strokeWidth / 2f = 12 +20 - 1.5 =30.5 约占20%
            posInfo3.endX = viewWidth - strokeWidth / 2f;
            posInfo3.endY = 0;

            posInfoList.add(posInfo3);


            posInfo4.startX = viewWidth - strokeWidth / 2f;
            posInfo4.startY = 0;

            posInfo4.endX = viewWidth - strokeWidth / 2f;
            posInfo4.endY = verticalTranslateSpace;

            posInfoList.add(posInfo4);

            posInfo5.startX = viewWidth - strokeWidth / 2f;
            posInfo5.startY = verticalTranslateSpace;

            posInfo5.endX = thirdIvCenterX;
            posInfo5.endY = verticalTranslateSpace;

            posInfoList.add(posInfo5);

        }

        if (stageList.size() > 4) {
            path4.reset();
            path4.moveTo(viewWidth - rightSpace - halfIvWidth, verticalTranslateSpace);
            path4.lineTo(leftSpace + horizontalSpace + ivWidth + halfIvWidth, verticalTranslateSpace);
            pathList.add(path4);

            posInfo6.startX = thirdIvCenterX;
            posInfo6.startY = verticalTranslateSpace;

            posInfo6.endX = secondIvCenterX;
            posInfo6.endY = verticalTranslateSpace;

            posInfoList.add(posInfo6);

        }
        if (stageList.size() > 5) {
            path5.reset();
            path5.moveTo(leftSpace + horizontalSpace + ivWidth + halfIvWidth, verticalTranslateSpace);
            path5.lineTo(cornerRadius + (strokeWidth / 2f), verticalTranslateSpace);
            pathList.add(path5);

            posInfo7.startX = secondIvCenterX;
            posInfo7.startY = verticalTranslateSpace;

            posInfo7.endX = firstIvCenterX;
            posInfo7.endY = verticalTranslateSpace;

            posInfoList.add(posInfo7);

        }
        /**
         * 要添加4段线
         * 直角的话只需要添加3条线
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

            path6.lineTo(firstIvCenterX, 2 * verticalTranslateSpace);
            pathList.add(path6);


            posInfo8.startX = firstIvCenterX;
            posInfo8.startY = verticalTranslateSpace;

            posInfo8.endX = strokeWidth / 2f;
            posInfo8.endY = verticalTranslateSpace;

            posInfoList.add(posInfo8);

            posInfo9.startX = strokeWidth / 2f;
            posInfo9.startY = verticalTranslateSpace;

            posInfo9.endX = strokeWidth / 2f;
            posInfo9.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo9);

            posInfo10.startX = strokeWidth / 2f;
            posInfo10.startY = 2 * verticalTranslateSpace;

            posInfo10.endX = firstIvCenterX;
            posInfo10.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo10);
        }

        if (stageList.size() > 7) {
            path7.reset();
            path7.moveTo(firstIvCenterX, 2 * verticalTranslateSpace);
            path7.lineTo(secondIvCenterX, 2 * verticalTranslateSpace);
            pathList.add(path7);

            posInfo11.startX = firstIvCenterX;
            posInfo11.startY = 2 * verticalTranslateSpace;

            posInfo11.endX = secondIvCenterX;
            posInfo11.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo11);

        }
        if (stageList.size() > 8) {
            path8.reset();
            path8.moveTo(secondIvCenterX, 2 * verticalTranslateSpace);
            path8.lineTo(viewWidth, 2 * verticalTranslateSpace);
            pathList.add(path8);

            posInfo12.startX = secondIvCenterX;
            posInfo12.startY = 2 * verticalTranslateSpace;

            posInfo12.endX = thirdIvCenterX;
            posInfo12.endY = 2 * verticalTranslateSpace;

            posInfoList.add(posInfo12);
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
                    //mPaint.setColor(redLineColor);
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
        Log.i(TAG, "drawBitmap: index = " + index);
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
                time = (int) (segKey * 30) + "秒";
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
        Log.i(TAG, "drawPoint: index = " + index);
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
        Log.i(TAG, "onTouchEvent:  width = " + getWidth() + " , height = " + getHeight());
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent:  ACTION_DOWN x = " + event.getX() + " , y =" + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent:  ACTION_UP x = " + event.getX() + " , y =" + event.getY());
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

        float percent = 1f;
    }
}
