package com.hm.viewdemo.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/** An ImageSpan for a Drawable that is centered vertically in the line. */
public class CenteredImageSpan extends ImageSpan {

    private Drawable mDrawable;

    public CenteredImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize(
            Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetrics) {
        final Drawable drawable = getCachedDrawable();
        final Rect rect = drawable.getBounds();

        if (fontMetrics != null) {
            Paint.FontMetricsInt paintFontMetrics = paint.getFontMetricsInt();
            fontMetrics.ascent = paintFontMetrics.ascent;
            fontMetrics.descent = paintFontMetrics.descent;
            fontMetrics.top = paintFontMetrics.top;
            fontMetrics.bottom = paintFontMetrics.bottom;
        }

        return rect.right;
    }

    @Override
    public void draw(
            Canvas canvas,
            CharSequence text,
            int start,
            int end,
            float x,
            int top,
            int y,
            int bottom,
            Paint paint) {
        final Drawable drawable = getCachedDrawable();
        canvas.save();
        final int transY = (bottom - drawable.getBounds().bottom) / 2;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        if (mDrawable == null) {
            mDrawable = getDrawable();
        }
        return mDrawable;
    }
}