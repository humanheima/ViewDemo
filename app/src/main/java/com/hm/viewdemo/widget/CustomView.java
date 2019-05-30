package com.hm.viewdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hm.viewdemo.R;

/**
 * Created by dumingwei on 2017/4/18.
 * 自定义View构造函数和自定义属性详解
 */
public class CustomView extends View {

    private static final String TAG = "CustomView";
    //文字内容
    private String customText;
    //文字的颜色
    private int customColor;
    //文字的字体大小
    private int fontSize;
    //画笔
    private TextPaint textPaint;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context, attrs);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        //打印出所有的属性
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            Log.d(TAG, "attributeName: " + attrs.getAttributeName(i) +
                    ",attributeValue: " + attrs.getAttributeValue(i));
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView, R.attr.CustomView_Theme_Style, R.style.CustomView_Yellow_Style);
        fontSize = (int) ta.getDimension(R.styleable.CustomView_cus_font, 16);
        customText = ta.getString(R.styleable.CustomView_text);
        customColor = ta.getColor(R.styleable.CustomView_color, Color.BLUE);
        ta.recycle();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(customColor);
        textPaint.setTextSize(fontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: custom text=" + customText);
        canvas.drawText(customText, 0, customText.length(), 100, 100, textPaint);
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public void setCustomColor(int customColor) {
        this.customColor = customColor;
        textPaint.setColor(customColor);
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        textPaint.setTextSize(fontSize);
    }
}
