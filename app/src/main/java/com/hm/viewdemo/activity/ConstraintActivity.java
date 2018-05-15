package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

public class ConstraintActivity extends BaseActivity {


    public static void launch(Context context) {
        Intent starter = new Intent(context, ConstraintActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_constraint_another;
    }

    @Override
    protected void initData() {

    }
}
