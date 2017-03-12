package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hm.viewdemo.R;
import com.hm.viewdemo.adapter.RecycleViewAdapter;
import com.hm.viewdemo.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MaxHeightLayoutActivity extends BaseActivity {

    @BindView(R.id.btn_show_pop_window)
    Button btnShowPopWindow;
    @BindView(R.id.btn_show_much_pop_window)
    Button btnShowMuchPopWindow;

    private PopupWindow popWindow;
    private PopupWindow muchPopWindow;
    private List<String> lessDataList;
    private List<String> muchDataList;
    private RecycleViewAdapter lessAdapter;
    private RecycleViewAdapter muchAdapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, MaxHeightLayoutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_max_height_layout;
    }

    @Override
    protected void initData() {
        lessDataList = new ArrayList<>();
        lessDataList.add("三国演义");
        lessDataList.add("水浒传");
        lessDataList.add("红楼梦");

        muchDataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            muchDataList.add("much data" + i);
        }
        lessAdapter = new RecycleViewAdapter(lessDataList, this);
        muchAdapter = new RecycleViewAdapter(muchDataList, this);
    }

    @OnClick({R.id.btn_show_pop_window, R.id.btn_show_much_pop_window})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show_pop_window:
                showPop();
                break;
            case R.id.btn_show_much_pop_window:
                showMuchPop();
                break;
        }
    }

    private void showPop() {
        if (popWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop, null);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(lessAdapter);
            popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popWindow.setBackgroundDrawable(new ColorDrawable());
            popWindow.showAtLocation(btnShowPopWindow, Gravity.BOTTOM, 0, 0);
        } else {
            popWindow.showAtLocation(btnShowPopWindow, Gravity.BOTTOM, 0, 0);
        }
    }

    private void showMuchPop() {
        if (muchPopWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.pop, null);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(muchAdapter);
            muchPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            muchPopWindow.setFocusable(true);
            muchPopWindow.setBackgroundDrawable(new ColorDrawable());
            muchPopWindow.showAtLocation(btnShowPopWindow, Gravity.BOTTOM, 0, 0);
        } else {
            muchPopWindow.showAtLocation(btnShowPopWindow, Gravity.BOTTOM, 0, 0);
        }
    }
}
