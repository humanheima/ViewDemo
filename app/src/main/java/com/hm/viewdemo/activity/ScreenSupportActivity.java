package com.hm.viewdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.hm.viewdemo.R;

public class ScreenSupportActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ScreenSupportActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_support);
    }
}
