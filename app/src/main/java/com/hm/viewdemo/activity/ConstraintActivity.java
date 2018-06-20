package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

import butterknife.BindView;

public class ConstraintActivity extends BaseActivity {

    @BindView(R.id.cl_root)
    ConstraintLayout clRoot;
    private Button btnOk;
    private Button btnCancel;

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
