package com.hm.viewdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by dumingwei on 2017/2/26.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindLayout());
        ButterKnife.bind(this);
    }

    protected abstract int bindLayout();

    protected abstract void initData();

    protected void bindEvent() {

    }
}
