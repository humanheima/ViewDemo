package com.hm.viewdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by dmw on 2018/11/27.
 * Desc:
 */
public class TestLayout extends LinearLayout {

    private final String TAG = getClass().getSimpleName();

    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param l View 向右滑动时为负值，向左滑动时为正值。
     * @param t View 向下滑动时为负值，向上滑动时为正值。
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(TAG, "onScrollChanged: l=" + l + ",t=" + t + ",oldl=" + oldl + ",oldt=" + oldt);
    }
}
