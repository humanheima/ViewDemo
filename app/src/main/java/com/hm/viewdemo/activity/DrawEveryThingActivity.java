package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hm.viewdemo.R;

public class DrawEveryThingActivity extends AppCompatActivity {


    public static void launch(Context context) {
        Intent intent = new Intent(context, DrawEveryThingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_every_thing);
    }
}
