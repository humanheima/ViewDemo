package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hm.viewdemo.R;
import com.hm.viewdemo.widget.CustomView;

public class CustomerViewActivity extends AppCompatActivity {

    private CustomView customView;

    public static void launch(Context context) {
        Intent starter = new Intent(context, CustomerViewActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_view);
        customView = new CustomView(this);
    }

}
