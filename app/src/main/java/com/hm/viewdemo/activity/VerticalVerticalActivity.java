package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.databinding.ActivityVerticalVerticalBinding;
import com.hm.viewdemo.widget.StickyLayout;
import java.util.ArrayList;
import java.util.List;

public class VerticalVerticalActivity extends BaseActivity<ActivityVerticalVerticalBinding> {

    private List<String> dataList;
    private ArrayAdapter<String> adapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, VerticalVerticalActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected ActivityVerticalVerticalBinding createViewBinding() {
        return ActivityVerticalVerticalBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add("data" + i);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        binding.stickyContent.setAdapter(adapter);
        binding.stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                if (binding.stickyContent.getFirstVisiblePosition() == 0) {
                    View view = binding.stickyContent.getChildAt(0);
                    if (view != null && view.getTop() >= 0) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
