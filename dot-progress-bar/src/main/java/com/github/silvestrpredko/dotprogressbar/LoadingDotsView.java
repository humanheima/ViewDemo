// LoadingDotsView.java
package com.github.silvestrpredko.dotprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LoadingDotsView extends View {

    private Paint paint;
    private int dotCount = 3;
    private float[] dotSizes;
    private float maxDotSize;
    private float minDotSize;
    private int currentDotIndex = 0;
    private long animationDuration = 600;
    private boolean isAnimating = true;

    public LoadingDotsView(Context context) {
        super(context);
        init();
    }

    public LoadingDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingDotsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF00D9D9); // Circle color
        maxDotSize = 40; // Maximum size of dots
        minDotSize = 20; // Minimum size of dots
        dotSizes = new float[dotCount];
        
        // Start animation
        startAnimation();
    }

    private void startAnimation() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAnimating) {
                    currentDotIndex = (currentDotIndex + 1) % dotCount;
                    for (int i = 0; i < dotCount; i++) {
                        dotSizes[i] = (i == currentDotIndex) ? maxDotSize : minDotSize;
                    }
                    invalidate(); // Redraw the view
                    postDelayed(this, animationDuration); // Repeat animation
                }
            }
        }, animationDuration);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float spacing = width / (dotCount + 1); // Space between dots

        for (int i = 0; i < dotCount; i++) {
            float x = (i + 1) * spacing;
            float y = getHeight() / 2;
            canvas.drawCircle(x, y, dotSizes[i] / 2, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Recalculate positions if needed
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAnimating = false; // Stop animation when detached
    }
}