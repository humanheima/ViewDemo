package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class DraggableViewGroup extends FrameLayout {
    private float lastX; // 记录上一次触摸的 X 坐标
    private float lastY; // 记录上一次触摸的 Y 坐标

    public DraggableViewGroup(Context context) {
        super(context);
    }

    public DraggableViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraggableViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录初始触摸点
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                // 计算位移
                float deltaX = x - lastX;
                float deltaY = y - lastY;

                // 计算新的位置
                float newX = getTranslationX() + deltaX;
                float newY = getTranslationY() + deltaY;

                // 获取屏幕尺寸
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;

                // 限制拖动范围
                if (newX >= 0 && newX <= screenWidth - getWidth()) {
                    setTranslationX(newX);
                }
                if (newY >= 0 && newY <= screenHeight - getHeight()) {
                    setTranslationY(newY);
                }

                // 更新最后触摸点
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_UP:
                // 吸附到屏幕左边或右边
                int screenWidthUp = getResources().getDisplayMetrics().widthPixels;
                if (getTranslationX() < (float) screenWidthUp / 2) {
                    animate().translationX(0).setDuration(200).start();
                } else {
                    animate().translationX(screenWidthUp - getWidth()).setDuration(200).start();
                }
                break;
        }
        return true; // 消费触摸事件
    }
}