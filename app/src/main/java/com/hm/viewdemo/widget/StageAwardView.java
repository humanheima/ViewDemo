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
    private Path pinkLinePath = new Path();
    private Path redLinePath = new Path();

    private int cornerRadius;
    private RectF rectF = new RectF();

    //大圆点半径
    private float bigPointRadius;
    //小圆点半径
    private float smallPointRadius;


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
        viewHeight = rowHeight * row + timeBottomToNextLineTop * (row - 1);
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

        /**
         * 开始画线
         */
        canvas.translate(0, -row * verticalTranslateSpace);

        if (row % 2 != 0) {//这种情况下，要向左移动
            canvas.translate(-2 * horizontalTranslateSpace, 0);
            canvas.translate(-leftSpace, 0);
        }
        canvas.translate(0, lineTopToIvTopHeight);

        pinkLinePath.reset();
        mPaint.setColor(pinkLineColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        /**
         * 添加第一条水平的线
         */

        float leftArcEdge = cornerRadius + (strokeWidth / 2f);

        //每行最右边第三张张图片的右边再减去曲线宽度的一半
        float rightEdge = viewWidth - cornerRadius - (strokeWidth / 2f);
        pinkLinePath.lineTo(rightEdge, 0);

        /**
         *右侧弧线加第二条横线
         */
        if (row > 1) {
            rectF.set(rightEdge - cornerRadius, 0, rightEdge + cornerRadius, 2 * cornerRadius);
            pinkLinePath.arcTo(rectF, -90f, 90f);

            pinkLinePath.lineTo(viewWidth - (strokeWidth / 2f), verticalTranslateSpace - cornerRadius);

            rectF.set(rightEdge - cornerRadius, verticalTranslateSpace - 2 * cornerRadius,
                    rightEdge + cornerRadius, verticalTranslateSpace);
            pinkLinePath.arcTo(rectF, 0f, 90f);

            if (row <= 2) {
                pinkLinePath.lineTo(0, verticalTranslateSpace);
            } else {
                pinkLinePath.lineTo(leftArcEdge, verticalTranslateSpace);
            }
        }
        if (row > 2) {
            rectF.set((strokeWidth / 2f), verticalTranslateSpace,
                    2 * cornerRadius + (strokeWidth / 2f),
                    verticalTranslateSpace + 2 * cornerRadius);
            pinkLinePath.arcTo(rectF, -90f, -90f);

            pinkLinePath.lineTo((strokeWidth / 2f), 2 * verticalTranslateSpace - 2 * cornerRadius);

            rectF.set((strokeWidth / 2f), 2 * verticalTranslateSpace - 2 * cornerRadius,
                    2 * cornerRadius + (strokeWidth / 2f),
                    2 * verticalTranslateSpace);

            pinkLinePath.arcTo(rectF, -180f, -90f);

            pinkLinePath.lineTo(viewWidth, 2 * verticalTranslateSpace);
        }
        canvas.drawPath(pinkLinePath, mPaint);

        /**
         * 画点
         */
        drawPoints(canvas);

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
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
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
        Log.d(TAG, "drawBitmap: index = " + index);
        if (index < stageList.size()) {

            //canvas.translate(halfIvWidth - pointRadius / 2f, -pointRadius / 2f);
            //canvas.translate(-dp5, 0);
            canvas.translate(halfIvWidth, 0);
            SendOptionsBean optionsBean = stageList.get(index);
           /* if (optionsBean.canGet()) {
                canvas.drawBitmap(canGetBitmap, 0, 0, mPaint);
                mPaint.setColor(coinColor);
            } else if (optionsBean.haveGot()) {
                canvas.drawBitmap(haveGotBitmap, 0, 0, mPaint);
                mPaint.setColor(Color.WHITE);
            } else {
                canvas.drawBitmap(cannotGetBitmap, 0, 0, mPaint);
                mPaint.setColor(coinColor);
            }*/

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(pinkLineColor);
            canvas.drawCircle(0, 0, bigPointRadius, mPaint);

            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(0, 0, smallPointRadius, mPaint);

            canvas.translate(-halfIvWidth, 0);

            //canvas.translate(-(halfIvWidth - pointRadius / 2f), pointRadius / 2f);
            //canvas.translate(dp5, 0);


        }
    }

}
