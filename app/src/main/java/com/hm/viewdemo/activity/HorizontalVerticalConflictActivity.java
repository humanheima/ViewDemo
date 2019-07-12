package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.base.BaseActivity;
import com.hm.viewdemo.util.MyUtils;
import com.hm.viewdemo.widget.HorizontalScrollViewEx;

import java.util.ArrayList;

import butterknife.BindView;

public class HorizontalVerticalConflictActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.horizontalScrollViewEx)
    HorizontalScrollViewEx horizontalScrollViewEx;

    public static void launch(Context context) {
        Intent starter = new Intent(context, HorizontalVerticalConflictActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_horizontal_vertical_conflict;
    }

    @Override
    protected void initData() {
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int screenHeight = MyUtils.getScreenMetrics(this).heightPixels;
        for (int i = 0; i < 3; i++) {
            ViewGroup layout = (ViewGroup) LayoutInflater.from(this)
                    .inflate(R.layout.content_layout, horizontalScrollViewEx, false);
            layout.getLayoutParams().width = screenWidth;
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0));
            TextView textView = layout.findViewById(R.id.title);
            textView.setText("page" + (i + 1));
            createList(layout);
            horizontalScrollViewEx.addView(layout);
        }
    }

    private void createList(ViewGroup layout) {
        ListView listView = layout.findViewById(R.id.list);
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("name" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.content_list_item, R.id.name, data);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HorizontalVerticalConflictActivity.this, "click item",
                        Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);
    }

}
