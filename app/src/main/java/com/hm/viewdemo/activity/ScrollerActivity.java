package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.TestScrollerView;

public class ScrollerActivity extends AppCompatActivity {

    private TestScrollerView testScrollerView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ScrollerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
    }
}
