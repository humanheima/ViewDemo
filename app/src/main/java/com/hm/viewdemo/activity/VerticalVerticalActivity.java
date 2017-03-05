package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.widget.StickyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VerticalVerticalActivity extends BaseActivity {

    @BindView(R.id.sticky_header)
    TextView stickyHeader;
    @BindView(R.id.sticky_content)
    ListView stickyContent;
    @BindView(R.id.sticky_layout)
    StickyLayout stickyLayout;

    private List<String> dataList;
    private ArrayAdapter<String> adapter;

    public static void launch(Context context) {
        Intent starter = new Intent(context, VerticalVerticalActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_vertical_vertical;
    }

    @Override
    protected void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            dataList.add("data" + i);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        stickyContent.setAdapter(adapter);
        stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
            @Override
            public boolean giveUpTouchEvent(MotionEvent event) {
                if (stickyContent.getFirstVisiblePosition()==0){
                    View view=stickyContent.getChildAt(0);
                    if (view!=null&&view.getTop()>=0){
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
