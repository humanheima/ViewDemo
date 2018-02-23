package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.hm.viewdemo.R;
import com.hm.viewdemo.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dumingwei on 2017/5/17.
 */
public class AutoWrapTextView extends View {

    private Paint paint;
    private int textSize;
    private int textColor;
    private int lineSpacingExtra;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    //文本的char数组
    private char[] textCharArray;
    //拆分后的文本集合
    private List<String> splitTextList;
    //单行文本显示的宽度
    private int singleTextWidth;
    private Rect[] splitTextRectArray;


    public AutoWrapTextView(Context context) {
        this(context, null);
    }

    public AutoWrapTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomAttrs(context, attrs);
        initPaint();
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoWrapTextView);
        paddingLeft = ta.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingLeft, 0);
        paddingTop = ta.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingTop, 0);
        paddingRight = ta.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingRight, 0);
        paddingBottom = ta.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingBottom, 0);
        textColor = ta.getColor(R.styleable.AutoWrapTextView_textColor, 0x000000);
        textSize = ta.getDimensionPixelSize(R.styleable.AutoWrapTextView_textSize, ScreenUtil.dpToPx(context, 16));
        lineSpacingExtra = ta.getInteger(R.styleable.AutoWrapTextView_lineSpacingExtra, 7);
        ta.recycle();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text))
            return;
        textCharArray = text.toCharArray();
        requestLayout();
    }

    private void splitText(int heightMode) {
        if (textCharArray == null)
            return;
        splitTextList = new ArrayList<>();
        singleTextWidth = getMeasuredWidth() - paddingLeft - paddingRight;
        int currentSingleTextWidth = 0;
        StringBuffer lineStringBuffer = new StringBuffer();
        for (int i = 0; i < textCharArray.length; i++) {
            char textChar = textCharArray[i];
            currentSingleTextWidth += getSingleCharWidth(textChar);
            if (currentSingleTextWidth >= singleTextWidth) {
                splitTextList.add(lineStringBuffer.toString());
                lineStringBuffer = new StringBuffer();
                currentSingleTextWidth = 0;
                i--;
            } else {
                lineStringBuffer.append(textChar);
                if (i == textCharArray.length - 1) {
                    splitTextList.add(lineStringBuffer.toString());
                }
            }
        }

        int textHeight = 0;
        splitTextRectArray = new Rect[splitTextList.size()];
        for (int m = 0, length = splitTextList.size(); m < length; m++) {
            String lineText = splitTextList.get(m);
            Rect lineTextRect = new Rect();
            paint.getTextBounds(lineText, 0, lineText.length(), lineTextRect);
            if (heightMode == MeasureSpec.AT_MOST) {
                textHeight += (lineTextRect.height() + lineSpacingExtra);
                if (m == length - 1) {
                    textHeight = textHeight + paddingBottom + paddingTop;
                }
            } else {
                if (textHeight == 0)
                    textHeight = getMeasuredHeight();
            }
            splitTextRectArray[m] = lineTextRect;
        }

        setMeasuredDimension(getMeasuredWidth(), textHeight);

    }

    private float getSingleCharWidth(char textChar) {
        float[] width = new float[1];
        paint.getTextWidths(new char[]{textChar}, 0, 1, width);
        return width[0];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        splitText(MeasureSpec.getMode(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (splitTextList == null || splitTextList.isEmpty())
            return;
        int marginTop = getTopTextMarginTop();
        for (int m = 0, length = splitTextList.size(); m < length; m++) {
            String lineText = splitTextList.get(m);
            canvas.drawText(lineText, paddingLeft, marginTop, paint);
            marginTop += splitTextRectArray[m].height() + lineSpacingExtra;
        }
    }

    private int getTopTextMarginTop() {
        return splitTextRectArray[0].height() / 2 + paddingTop + getFontSpace();
    }

    private int getFontSpace() {
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        return (fontMetricsInt.descent - fontMetricsInt.ascent) / 2 - fontMetricsInt.descent;
    }
}
