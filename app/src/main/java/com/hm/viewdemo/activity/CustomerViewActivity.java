package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

public class CustomerViewActivity extends BaseActivity {


    public static void launch(Context context) {
        Intent starter = new Intent(context, CustomerViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_custome_view;
    }

    @Override
    protected void initData() {

    }
}
