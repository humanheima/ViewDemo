package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by dumingwei on 2018/5/8 0008.
 * 不消耗 touch 事件的ScrollView
 */
public class NoConsumeScrollView extends ScrollView {

    public NoConsumeScrollView(Context context) {
        super(context);
    }

    public NoConsumeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoConsumeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
