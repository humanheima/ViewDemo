package com.hm.viewdemo.activity.design;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by p_dmweidu on 2024/9/25
 * Desc:
 */
public class Demo1Behavior extends CoordinatorLayout.Behavior<View> {


    private static final String TAG = "Demo1Behavior";


    public Demo1Behavior() {
        super();
    }

    public Demo1Behavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //这里判断dependency所属的View是哪一个,返回true，onDependentViewChanged才执行,否则不执行
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        Log.e(TAG, "onDependentViewChanged:  parent=" + parent + "\n child=" + child + "\n dependency=" + dependency);
        /*
         *这里获取dependency的top值,也就是AppBarLayout的top,因为AppBarLayout
         *在是向上滚出界面的,我们的因为是和AppBarLayout相反,所以取绝对值.
         */
        float translationY = Math.abs(dependency.getTop());
        child.setTranslationY(translationY);
        return true;
    }
}
