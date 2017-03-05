package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.CustomRecyclerViewAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.MyNestedScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScrollViewNestRecyclerViewActivity extends BaseActivity {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.activity_scroll_view_nest_recycler_view)
    MyNestedScrollView activityScrollViewNestRecyclerView;

    private List<String> dataList;
    private CustomRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ScrollViewNestRecyclerViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_scroll_view_nest_recycler_view;
    }

    @Override
    protected void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add("data" + i);
        }
        layoutManager = new LinearLayoutManager(this);
        adapter = new CustomRecyclerViewAdapter(this, dataList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            textTitle.setFocusable(true);
            textTitle.setFocusableInTouchMode(true);
            textTitle.requestFocus();
        }
    }
}
