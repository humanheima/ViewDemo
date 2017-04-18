package com.hm.viewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dumingwei on 2017/4/18.
 * 自定义View构造函数和自定义属性详解
 */
public class CustomView extends View {

    private static final String TAG = "CustomView";

    public CustomView(Context context) {
        super(context);
        Log.e(TAG, "CustomView: 一个参数的构造函数");
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "CustomView: 2个参数的构造函数");

    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG, "CustomView: 3个参数的构造函数");

    }
}
