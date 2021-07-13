package com.capton.colorfulprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * 这是一个绘制了多个等腰直角三角形的View，呈现出一个由很多个斜条纹组成的双色画布
 * 可以自行通过new ColorfulView(Context context,int width,Paint paint,Paint paint2)方法，构建出来看效果
 * Created by capton on 2017/8/10.
 */

public class ColorfulView extends View {


    private Paint paint;
    private Paint paint2;
    private int mWidth;

    private Path p1 = new Path();
    private Path p2 = new Path();

    public ColorfulView(Context context) {
        super(context);
    }

    public ColorfulView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfulView(Context context, int width, Paint paint, Paint paint2) {
        super(context);
        mWidth = width;
        this.paint = paint;
        this.paint2 = paint2;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setPaint2(Paint paint2) {
        this.paint2 = paint2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x, y;
        float x2, y2;

        int tempWidth = mWidth;
        int tempWidth2 = 0;


        for (int i = 0; i < 50; i++) {
            p1.rewind();
            if (i % 2 == 0) {//每个减去20,0的时候不用减
                if (i != 0) {
                    tempWidth -= 10;
                }
            } else {
                tempWidth -= 30;
            }
            x = tempWidth;
            y = tempWidth;
            p1.lineTo(0, y);
            p1.lineTo(x, 0);
            p1.lineTo(0, 0);
            p1.close();
            //x = ((float) mWidth / 20) * i;
            // y = ((float) mWidth / 20) * i;

            if (i % 2 == 0) {
                canvas.drawPath(p1, paint);
            } else {
                canvas.drawPath(p1, paint2);
            }

            if (tempWidth < 0) {
                break;
            }
        }

        for (int i = 0; i < 50; i++) {
            p2.rewind();
            p2.moveTo(mWidth, mWidth);

            if (i % 2 == 0) {//每个减去20,0的时候不用减
                if (i != 0) {
                    tempWidth2 += 30;
                }
            } else {
                tempWidth2 += 10;
            }
            x2 = tempWidth2;
            y2 = tempWidth2;
            p2.lineTo(mWidth, y2);
            p2.lineTo(x2, mWidth);
            p2.lineTo(mWidth, mWidth);
            p2.close();
            //x = ((float) mWidth / 20) * i;
            // y = ((float) mWidth / 20) * i;

            if (i % 2 != 0) {
                canvas.drawPath(p2, paint);
            } else {
                canvas.drawPath(p2, paint2);
            }

            if (tempWidth2 > mWidth) {
                break;
            }
        }

//        for (int i = 0; i < 20; i++) {
//            p2.rewind();
//            x2 = y2 = ((float) mWidth / 20) * i;
//            p2.moveTo(mWidth, mWidth);
//            p2.lineTo(mWidth, y2);
//            p2.lineTo(x2, mWidth);
//            p2.lineTo(mWidth, mWidth);
//            p2.close();
//            if (i % 2 != 0) {
//                canvas.drawPath(p2, paint);
//            } else {
//                canvas.drawPath(p2, paint2);
//            }
//        }
    }


}
