package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.TestLayout;
import com.hm.viewdemo.widget.TestScrollerView;

public class ScrollerActivity extends AppCompatActivity {

    int i = 0;
    private TestScrollerView testScrollerView;
    private Button btnScroll;
    private TestLayout testLayout;
    private Button btnStartScroll;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ScrollerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        testLayout = findViewById(R.id.testLayout);
        btnScroll = findViewById(R.id.btnScroll);
        btnStartScroll = findViewById(R.id.btnStartScroll);
        btnStartScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testLayout.scrollBy(-20, -20);
                testLayout.scrollBy(20, 20);
            }
        });
    }
}
