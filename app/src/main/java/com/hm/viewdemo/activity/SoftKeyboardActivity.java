package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.hm.viewdemo.R;

public class SoftKeyboardActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "SoftKeyboardActivity";

    private View activityRootView;

    //设定一个认为是软键盘弹起的阈值
    private int softKeyboardHeight;
    private int heightPixels;

    public static void launch(Context context) {
        Intent intent = new Intent(context, SoftKeyboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_keyboard);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //设定一个认为是软键盘弹起的阈值
        softKeyboardHeight = (int) (100 * dm.density);

        //屏幕高度
        heightPixels = dm.heightPixels;

        Log.d(TAG, "onCreate: softKeyboardHeight= " + softKeyboardHeight + ",heightPixels= " + heightPixels);

        activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (isKeyboardShown(activityRootView)) {
            Log.e(TAG, "软键盘弹起");
        } else {
            Log.e(TAG, "软键盘关闭");
        }
    }

    /*private boolean isKeyboardShown(View rootView) {
        //得到屏幕可见区域的大小
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        //rootView 的bottom和当前屏幕可见区域（包括状态栏）bottom的差值
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight;
    }*/

    private boolean isKeyboardShown(View rootView) {
        //得到屏幕可见区域的大小
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        int heightDiff = heightPixels - r.bottom;
        return heightDiff > softKeyboardHeight;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
