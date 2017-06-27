package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;

import butterknife.BindView;

public class ViewDragHelperActivity extends BaseActivity {

    private static final String TAG = "ViewDragHelperActivity";
    @BindView(R.id.text_view)
    TextView textView;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ViewDragHelperActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_view_drag_helper;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void bindEvent() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewDragHelperActivity.this, "可拖动控件的点击事件", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
