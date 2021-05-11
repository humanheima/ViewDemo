package com.hm.viewdemo.widget.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;


/**
 * Created by dumingwei on 2021/5/11
 * <p>
 * Desc:
 */
public class SpaceSpan extends ReplacementSpan {

    private int mWidth;

    public SpaceSpan(int width) {

        mWidth = width;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        //do nothing
    }
}
