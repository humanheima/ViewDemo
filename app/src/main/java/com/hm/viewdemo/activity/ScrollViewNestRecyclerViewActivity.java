package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.hm.viewdemo.adapter.CustomRecyclerViewAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityScrollViewNestRecyclerViewBinding;
import java.util.ArrayList;
import java.util.List;

public class ScrollViewNestRecyclerViewActivity extends BaseActivity<ActivityScrollViewNestRecyclerViewBinding> {

    private List<String> dataList;
    private CustomRecyclerViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static void launch(Context context) {
        Intent starter = new Intent(context, ScrollViewNestRecyclerViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityScrollViewNestRecyclerViewBinding createViewBinding() {
        return ActivityScrollViewNestRecyclerViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add("data" + i);
        }
        layoutManager = new LinearLayoutManager(this);
        adapter = new CustomRecyclerViewAdapter(this, dataList);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            binding.textTitle.setFocusable(true);
            binding.textTitle.setFocusableInTouchMode(true);
            binding.textTitle.requestFocus();
        }
    }
}
