package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.SimpleFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class SimpleFlowLayoutActivity extends AppCompatActivity {

    private SimpleFlowLayout simpleFlowLayout;

    public static void launch(Context context) {
        Intent intent = new Intent(context, SimpleFlowLayoutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_flow_layout);
        simpleFlowLayout = findViewById(R.id.simple_flow_layout);
        // 关键字集合
        List<String> list = new ArrayList<>();
        list.add("dumingweidumingwei");
        list.add("key二");
        list.add("杜明伟");
        list.add("关键4");
        list.add("关键词五");
        list.add("关键词五");
        list.add("关键词五");
        list.add("关键词五");
        list.add("关键词五");
        list.add("关键词五");
        list.add("关键词五");
        simpleFlowLayout.setViews(list, new SimpleFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(SimpleFlowLayoutActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
